package miyagi

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.ContentType

import net.sf.json.JsonConfig
import net.sf.json.JSONObject
import net.sf.json.util.CycleDetectionStrategy

class ElasticService {

    def elastic(method, url, reqBody) {
        def http = new HTTPBuilder(url)
        http.request(method, ContentType.JSON) { req ->
            body = reqBody
            response.success = { resp, data -> data }
            response.failure = { resp, data ->
                log.info "FAILED ${method} ${url} ${reqBody}"
                log.info data.toString()
            }
        }
    }

    def getAllPeople() {
        elastic(
            Method.POST,
            'http://localhost:9200/people/_search', 
            [
                query: [
                    match_all: []
                ]
            ]
        )
    }

    def savePerson(Person p) {
        elastic(
            Method.POST,
            'http://localhost:9200/people/_doc',
            p.toObj()
        )
    }

    def search(name, startDate, endDate) {
        log.info "ElasticService.search(${name}, ${startDate}, ${endDate})"

        // Why does this work?
        if (!startDate?.trim()) {
            startDate = null
        }
        if (!endDate?.trim()) {
            endDate = null
        }

        def nameQuery = [
            bool: [
                should: [
                    [
                        match: [
                            firstName: name 
                        ] 
                    ], [
                        match: [
                            lastName: name
                        ]
                    ]
                ]
            ]
        ]
        def dateQuery = [
            range: [
                dob: [
                    gte: startDate,
                    lte: endDate
                ]
            ] 
        ]

        def queries = [dateQuery]

        if (name?.trim()) {
            queries.add(nameQuery)
        }

        def searchObj = [
            query: [
                bool: [
                    must: queries
                ]
            ]
        ]

        log.info searchObj.toString()

        def results = elastic(
            Method.POST,
            'http://localhost:9200/people/_search', 
            searchObj
        )
        log.info results.toString()

        return restructureResults(results)
    }

    def restructureResults(results) {
        results = results.hits.hits
        results.collect {
            item -> item._source
        }
    }
}

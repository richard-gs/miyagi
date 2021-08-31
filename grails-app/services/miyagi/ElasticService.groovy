package miyagi

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.ContentType

import net.sf.json.JsonConfig
import net.sf.json.JSONObject
import net.sf.json.util.CycleDetectionStrategy

class ElasticService {

    def utilService

    def elastic(method, url, reqBody) {
        def http = new HTTPBuilder(url)
        http.request(method, ContentType.JSON) { req ->
            body = reqBody
            response.success = { resp, data -> data }
            response.failure = { resp, data ->
                log.info 'FAILED'
                log.info resp.toString()
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

        log.info "wow ElasticService.search(${name}, ${startDate}, ${endDate})"

        // (name, startDate, endDate) = utilService.normalizeInputs(
        //     name, startDate, endDate)

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
        def searchObj = [
            query: [
                bool: [
                    must: [
                        nameQuery,
                        dateQuery
                    ]
                ]
            ]
        ]

        log.info searchObj.toString()

        def results = elastic(
            Method.POST,
            'http://localhost:9200/people/_search', 
            searchObj
        )

        return restructureResults(results)
    }

    def restructureResults(results) {
        results = results.hits.hits
        results.collect {
            item -> item._source
        }
    }
}

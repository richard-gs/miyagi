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

}

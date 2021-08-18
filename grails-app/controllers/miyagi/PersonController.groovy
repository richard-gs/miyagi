package miyagi

import grails.rest.*
import grails.converters.*

class PersonController {
    static responseFormats = ['json']

    def index() {
        log.info "Params: ${params.toString()}"
        respond Person.list()
    }

    def show() {
        respond Person.get(params.id)
    }

    def save() {
        log.info params.toString()
        def u = new Person(firstName:"Darth", lastName:"Vader")
        u.save(flush: true, failOnError: true)
        respond u
    }

    // Test with:
    // curl -i -X PUT -H "Content-Type: application/json" -d '{"name":"asdf"}' localhost:8080/person/6
    def update() {
        log.info "update() params: ${params.toString()} | request: ${request.JSON}"
        respond(['ok'])
    }

    def delete() {
        def p = Person.get(params.id);
        p.delete(flush: true)
        respond p
    }
}

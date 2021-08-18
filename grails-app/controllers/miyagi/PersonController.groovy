package miyagi

import grails.rest.*
import grails.converters.*

class PersonController {
    static responseFormats = ['json']

    def index() {
        respond(Person.list()) // http://docs.grails.org/3.3.11/guide/single.html#jsonResponses
    }

    def show() {
        respond(Person.get(params.id))
    }

    /**
     * http://docs.grails.org/3.3.11/guide/theWebLayer.html#commandObjects >> "Binding The Request Body To Command Objects"
     *
     * Test with (sad):
     * curl -i -X POST -H "Content-Type: application/json" -d '{"name":"asdf", "firstName":"first!"}' localhost:8080/person
     *
     * Test with (happy):
     * curl -i -X POST -H "Content-Type: application/json" -d '{"name":"asdf", "firstName":"first!", "lastName":"last!"}' localhost:8080/person
     */
    def save(Person p) {
        // https://docs.grails.org/3.3.11/guide/validation.html#validatingConstraints
        if (p.validate()) {
            p.save(flush: true, failOnError: true)
            respond(['ok': p])
        } else {
            log.warn p.errors.toString()
            response.sendError(400); // https://stackoverflow.com/questions/1429388/how-can-i-return-a-404-50x-status-code-from-a-grails-controller
        }
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
        respond(p)
    }
}

// http://docs.grails.org/3.3.11/guide/theWebLayer.html#dataBinding >> "Binding Request Data to the Model"

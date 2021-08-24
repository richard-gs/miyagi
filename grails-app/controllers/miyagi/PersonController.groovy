package miyagi

import grails.rest.*
import grails.converters.*
import org.hibernate.FetchMode

class PersonController {
    static responseFormats = ['json']

    def personToResponse(Person p) {
        if (!p) { return }
        def person = [
            id:        p.id,
            firstName: p.firstName,
            lastName:  p.lastName,
            dob:       p.dob,
        ]
        if (p.address) {
            person.street = p.address.street;
            person.city   = p.address.city;
            person.state  = p.address.state;
            person.zip    = p.address.zip;
        }
        return person
    }

    def index() {
        def response = []

        Person.list().each { p ->
            def person = personToResponse(p)
            response.push(person)
        }
        
        respond(response) // http://docs.grails.org/3.3.11/guide/single.html#jsonResponses
    }

    def show() {
        Person p = Person.get(params.id)
        def response = personToResponse(p)
        respond(response)
    }

    /**
     * http://docs.grails.org/3.3.11/guide/theWebLayer.html#commandObjects >> "Binding The Request Body To Command Objects"
     *
     * Test with (sad):
     * curl -i -X POST -H "Content-Type: application/json" -d '{"name":"asdf", "firstName":"first!"}' localhost:8080/person
     *
     * Test with (happy):
     * curl -i -X POST -H "Content-Type: application/json" -d '{"firstName":"another", "lastName":"person", "dob":"1993-03-24", "address": {"street":"123 fake st", "city":"anytown", "state":"md", "zip":"12345"}}' localhost:8080/person
     */
    def save(Person p) {
        log.info p.toString()
        saveUpdate(p)
    }

    /**
     * Test with:
     * curl -i -X PUT -H "Content-Type: application/json" -d '{"name":"asdf"}' localhost:8080/person/6
     */
    def update(Person newPerson) {
        Person existingPerson = Person.get(params.id)
        if (!existingPerson) {
            return response.sendError(404) 
        }
        existingPerson.properties = newPerson.properties
        saveUpdate(existingPerson)
    }

    def saveUpdate(Person p) {
        // https://docs.grails.org/3.3.11/guide/validation.html#validatingConstraints
        // https://docs.grails.org/3.3.11/ref/Domain%20Classes/save.html (auto validates on save)
        try {
            p.save(flush: true, failOnError: true)
            respond(['ok': p])
        } catch(grails.validation.ValidationException e) {
            log.error e.toString()
            response.sendError(400) // https://stackoverflow.com/questions/1429388/how-can-i-return-a-404-50x-status-code-from-a-grails-controller
        }
    }

    def delete() {
        def p = Person.get(params.id)
        p.delete(flush: true)
        respond(p)
    }

    def search() {
        log.info "SEARCH!!!"
        log.info params.name
        log.info params.startDate
        log.info params.endDate
        if (!params.name?.trim()) {
            params.name = "%"
        }
        if (!params.startDate?.trim()) {
            Calendar cal = Calendar.getInstance(); /// 
            cal.set(1, 1, 1);                      /// This is why java sucks
            params.startDate = cal.getTime();      /// 
        } else {
            params.startDate = Date.parse("yyyy-MM-dd", params.startDate)
        }
        if (!params.endDate?.trim()) {
            Calendar cal = Calendar.getInstance(); /// 
            cal.set(3000, 1, 1);                   /// This is why java sucks
            params.endDate = cal.getTime();        /// 
        } else {
            params.endDate = Date.parse("yyyy-MM-dd", params.endDate)
        }
        log.info "---"
        log.info  params.name
        log.info params.startDate.toString()
        log.info params.endDate.toString()
        // Person.findAllByFirstNameLikeOrLastNameLike
        // def results = Person.findAll {
        //     (firstName =~ params.name || lastName =~ params.name) && (params.startDate < dob) && (params.endDate > dob)
        // }
        def results = Person.createCriteria().list {
            or {
                ilike("firstName", "%$params.name%")
                ilike("lastName", "%$params.name%")
            }
            between("dob", params.startDate, params.endDate)
        }
        // respond(results)

        def response = []
        results.each { p ->
            def person = personToResponse(p)
            response.push(person)
        }
        respond(response)
    }

    // def groupBy() {
    //     Person.createCriteria().list {
    //         projections {
    //             sqlGroupProjection 
    //         }
    //     }
    //     sqlGroupProjection
    // }
}

// http://docs.grails.org/3.3.11/guide/theWebLayer.html#dataBinding >> "Binding Request Data to the Model"

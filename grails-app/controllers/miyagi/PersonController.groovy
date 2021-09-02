package miyagi

class PersonController {

    def personService
    def elasticService
    def googleMapsService

    /**
     * Test with: curl "localhost:8080/person"
     */
    def index() {
        log.info "Index"
        respond( personService.getAllIncludeAddress() ) // http://docs.grails.org/3.3.11/guide/single.html#jsonResponses
    }

    /**
     * Test with: curl "localhost:8080/person/123"
     */
    def show() {
        log.info "Show"

        def person = personService.getById(params.id)
        def location = googleMapsService.geocode(person.getAddressString())
        log.info location.dump()

        person = person.toObj()
        person.lat = location.results[0].geometry.location.lat;
        person.lon = location.results[0].geometry.location.lng;

        log.info person.dump()

        respond(person)
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
        log.info "Save"

        def dbResponse = personService.save(p)
        if (dbResponse == 400) {
            response.sendError(400) // https://stackoverflow.com/questions/1429388/how-can-i-return-a-404-50x-status-code-from-a-grails-controller
        }
        elasticService.savePerson(p)

        respond( ['ok': dbResponse] )
    }

    /**
     * Test with: curl -i -X PUT -H "Content-Type: application/json" -d '{"name":"asdf"}' localhost:8080/person/6
     */
    def update(Person newPerson) {
        log.info "Update"

        def dbResponse = personService.update(params.id, newPerson)

        respond( ['ok': dbResponse] )
    }

    /**
     * Test with: curl -XDELETE "localhost:8080/person/123"
     */
    def delete() {
        log.info "Delete"
        respond( personService.delete(params.id) )
    }

    /**
     * Test with: curl "localhost:8080/person/search?name=james"
     */
    def search() {
        log.info "Search"

        def response = personService.search(
            params.name,
            params.startDate,
            params.endDate
        )
        respond(response)
    }

    /**
     * Test with: curl "localhost:8080/person/elasticSearch?name=james&startDate=1800-01-01"
     */
    def elasticSearch() {
        log.info "Elastic Search"
        
        respond(
            elasticService.search(
                params.name,
                params.startDate,
                params.endDate
            )
        )
    }

}

// http://docs.grails.org/3.3.11/guide/theWebLayer.html#dataBinding >> "Binding Request Data to the Model"

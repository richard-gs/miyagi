package miyagi

import grails.rest.*
import grails.converters.*

class PersonController {
    static responseFormats = ['json']

    def index() {
        log.trace "PRINT TRACE"
        log.debug "PRINT DEBUG"
        log.info "PRINT INFO"
        log.warn "PRINT WARNING"
        log.error "PRINT ERROR"
        respond new Person(firstName:"Bob", lastName:"the Bulider")
    }

    def save() {
        log.error "wow"
        def u = new Person(firstName:"Darth", lastName:"Vader")
        u.save()
        respond u
    }
}

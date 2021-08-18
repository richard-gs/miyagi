package miyagi

import grails.rest.*
import grails.converters.*

class UserController {
    static responseFormats = ['json']

    def index() {
        log.trace "PRINT TRACE"
        log.debug "PRINT DEBUG"
        log.info "PRINT INFO"
        log.warn "PRINT WARNING"
        log.error "PRINT ERROR"
        respond new User(firstName:"Bob", lastName:"the Bulider")
    }

    def save() {
        log.error "wow"
        def u = new User(firstName:"Darth", lastName:"Vader")
        u.save()
        respond u
    }
}

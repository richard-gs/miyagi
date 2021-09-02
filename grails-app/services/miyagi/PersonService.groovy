package miyagi

import grails.gorm.transactions.Transactional

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.ContentType

import net.sf.json.JsonConfig
import net.sf.json.JSONObject
import net.sf.json.util.CycleDetectionStrategy

@Transactional
class PersonService {

    def getAllIncludeAddress() {
        def people = Person.where{}.join('address').list()
        return Person.toObj(people)
    }


    def getById(personId) {
        return Person.where{ id == personId }
            .join('address')
            .find()
    }


    def save(Person p) {
        log.info "Saving to database..."
        // https://docs.grails.org/3.3.11/guide/validation.html#validatingConstraints
        // https://docs.grails.org/3.3.11/ref/Domain%20Classes/save.html (auto validates on save)
        try {
            return p.save(failOnError: true)
        } catch(grails.validation.ValidationException e) {
            log.error e.toString()
            return 400
        }
    }


    def update(existingPersonId, Person newPerson) {
        Person existingPerson = Person.get(existingPersonId)
        if (!existingPerson) {
            return response.sendError(404) 
        }
        existingPerson.properties = newPerson.properties
        return save(existingPerson)
    }


    def delete(personId) {
        Person p = Person.where{ id == personId }.join('address').find()
        p.delete()
        p.address.delete()
        return p
    }

    def search(name, startDate, endDate) {
        log.info "PersonService.search(${name}, ${startDate}, ${endDate})"

        if (!name?.trim()) {
            name = "%"
        }
        if (!startDate?.trim()) {
            Calendar cal = Calendar.getInstance()
            cal.set(1, 1, 1);
            startDate = cal.getTime();
        } else {
            startDate = Date.parse("yyyy-MM-dd", startDate)
        }
        if (!endDate?.trim()) {
            Calendar cal = Calendar.getInstance()
            cal.set(3000, 1, 1);
            endDate = cal.getTime();
        } else {
            endDate = Date.parse("yyyy-MM-dd", endDate)
        }

        log.info "Normalized: ${name}, ${startDate}, ${endDate}"

        def people = Person.createCriteria().list {
            or {
                ilike("firstName", "%$name%")
                ilike("lastName", "%$name%")
            }
            between("dob", startDate, endDate)
        }

        log.info people.toString()

        return Person.toObj(people)
    }
}

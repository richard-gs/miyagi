package miyagi

import grails.databinding.BindingFormat
import groovy.transform.ToString

// select * from person p full join address a on a.person_id=p.id;
class Person {
    String firstName
    String lastName

    @BindingFormat('yyyy-MM-dd')
    Date dob

    Address address // http://gorm.grails.org/6.1.x/hibernate/manual/#domainClasses

    // http://docs.grails.org/3.3.11/guide/single.html#validation
    static constraints = {
        firstName blank: false
        lastName blank: false
        dob blank: false
        address nullable: true
    }

    // @ToString implementation is garbo and causes a stack overflow
    @Override
    public String toString() {
        return "${firstName}|${lastName}|${dob}|${address?.street}|${address?.city}|${address?.state}|${address?.zip}"
    }

    def toObj() {
        def obj = [
            id:        id,
            firstName: firstName,
            lastName:  lastName,
            dob:       dob.format('yyyy-MM-dd'),
            address: [
                id:     address.id,
                street: address.street,
                city:   address.city,
                state:  address.state,
                zip:    address.zip,
            ]
        ]
    }

    def getAddressString() {
        return "${address.street}, ${address.city}, ${address.state} ${address.zip},"
    }

    static def toObj(List<Person> people) {
        return people.collect {
            person -> person.toObj()
        }
    }
}

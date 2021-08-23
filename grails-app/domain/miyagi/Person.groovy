package miyagi

import grails.databinding.BindingFormat
import groovy.transform.ToString

// select * from person p full join address a on a.person_id=p.id;
// @ToString(includeNames=true)
class Person {
    String firstName
    String lastName

    @BindingFormat('yyyy-MM-dd')
    Date dob

    // http://gorm.grails.org/6.1.x/hibernate/manual/#domainClasses
    static hasOne = [address:Address]

    // http://docs.grails.org/3.3.11/guide/single.html#validation
    static constraints = {
        firstName blank: false
        lastName blank: false
        dob blank: false
    }
}

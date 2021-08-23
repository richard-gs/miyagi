package miyagi

import groovy.transform.ToString

// @ToString(includeNames=true)
class Address {
    String street
    String city
    String state
    String zip

    static belongsTo = [person:Person]

    static constraints = {
        street blank: false
        city blank: false
        state blank: false
        zip blank: false
    }

    // static mapping = {
    //     person lazy: false
    // }
}

package miyagi

import groovy.transform.ToString

class Address {
    String street
    String city
    String state
    String zip

    static constraints = {
        street blank: false
        city blank: false
        state blank: false
        zip blank: false
    }
}

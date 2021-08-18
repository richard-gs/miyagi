package miyagi

class Person {

    String firstName
    String lastName

    // http://docs.grails.org/3.3.11/guide/single.html#validation
    static constraints = {
        firstName blank: false
        lastName blank: false
    }
}

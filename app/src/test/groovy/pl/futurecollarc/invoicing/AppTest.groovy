package pl.futurecollarc.invoicing

import spock.lang.Specification

class AppTest extends Specification {
    def "GetGreeting"() {
        setup:
        def app = new App()

        when:
        def result = app.greeting

        then:
        result != null
    }

    def "dummy test to cover main"() {
        setup:
        def app = new App()

        and:
        app.main()
    }
}

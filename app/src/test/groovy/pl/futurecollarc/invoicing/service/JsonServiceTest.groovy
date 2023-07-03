package pl.futurecollarc.invoicing.service

import pl.futurecollarc.invoicing.TestHelper
import spock.lang.Specification

class JsonServiceTest extends Specification {

    def "shouldConvertObjectToJsonAndBack"() {

        given:
        JsonService jsonService = new JsonService()
        def invoice = TestHelper.invoice(1)

        when:
        def invoiceAsJson = jsonService.objectToJson(invoice)

        and:
        def invoiceFromJson = jsonService.jsonToObject(invoiceAsJson)

        then:
        invoice == invoiceFromJson
    }
}

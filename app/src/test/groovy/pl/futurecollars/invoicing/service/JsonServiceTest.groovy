package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

class JsonServiceTest extends Specification {

    def "shouldConvertObjectToJsonAndBack"() {

        given:
        JsonService jsonService = new JsonService()
        def invoice = TestHelper.invoice(1)

        when:
        def invoiceAsJson = jsonService.objectToJson(invoice)

        and:
        def invoiceFromJson = jsonService.jsonToObject(invoiceAsJson, Invoice.class)

        then:
        invoice == invoiceFromJson
    }
}

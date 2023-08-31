package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.db.Database
import spock.lang.Specification

class TaxCalculatorServiceTest extends Specification {
    private Database database = Mock()
    private TaxCalculatorService taxService = new TaxCalculatorService(database)

    def "shouldGetIncome"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber

        when:
        taxService.income(invoiceIdentificationNumber)

        then:
        1 * database.visit(_, _)
    }

    def "shouldGetCosts"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber

        when:
        taxService.costs(invoiceIdentificationNumber)

        then:
        1 * database.visit(_, _)
    }

    def "shouldGetIncomingVat"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber

        when:
        taxService.incomingVat(invoiceIdentificationNumber)

        then:
        1 * database.visit(_, _)
    }

    def "shouldGetOutgoingVat"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber

        when:
        taxService.outgoingVat(invoiceIdentificationNumber)

        then:
        1 * database.visit(_, _)
    }

//    def "shouldReturnTaxCalculator"(){ //TaxCalculatorService.income(String)" is null
//        given:
//        def expectedCalculateTaxes = TestHelper.taxCalculator()
//        def invoice = TestHelper.invoice(1)
//        def invoiceIdentificationNumber = invoice.getSeller()
//
//        when:
//        def calculateTaxes = taxService.calculateTaxes(invoiceIdentificationNumber)
//
//        then:
//        calculateTaxes == expectedCalculateTaxes
//    }
}

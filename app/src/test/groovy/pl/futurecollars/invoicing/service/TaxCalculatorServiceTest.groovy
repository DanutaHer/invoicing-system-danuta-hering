package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

class TaxCalculatorServiceTest extends Specification {

    private Database<Invoice> database = Mock()
    private TaxCalculatorService taxService = new TaxCalculatorService(database)

    def "shouldGetIncome"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber
        database.getAll() >> List.of(invoice)

        when:
        def expectedIncome = taxService.income(invoiceIdentificationNumber)

        then:
        expectedIncome == 1500
    }

    def "shouldGetCosts"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber
        database.getAll() >> List.of(invoice)

        when:
        def expectedCosts = taxService.costs(invoiceIdentificationNumber)

        then:
        expectedCosts == 1657.5
    }

    def "shouldGetIncomingVat"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber
        database.getAll() >> List.of(invoice)

        when:
        def expectedIncomingVat = taxService.incomingVat(invoiceIdentificationNumber)

        then:
        expectedIncomingVat == 315
    }

    def "shouldGetOutgoingVat"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber
        database.getAll() >> List.of(invoice)

        when:
        def expectedOutgoingVat = taxService.outgoingVat(invoiceIdentificationNumber)

        then:
        expectedOutgoingVat == 157.5
    }

    def "shouldGetEarnings"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber
        database.getAll() >> List.of(invoice)

        when:
        def expectedEarnings = taxService.getEarnings(invoiceIdentificationNumber)

        then:
        expectedEarnings == -157.5
    }

    def "shouldGetVatToPay"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber
        database.getAll() >> List.of(invoice)

        when:
        def expectedVatToPay = taxService.getVatToPay(invoiceIdentificationNumber)

        then:
        expectedVatToPay == 157.5
    }

    def "shouldCalculateTaxes"() {
        given:
        def invoice = TestHelper.invoice(1)
        def company = invoice.getSeller()
        database.getAll() >> List.of(invoice)

        when:
        def expectedCalculateTaxes = taxService.calculateTaxes(company)

        then:
        expectedCalculateTaxes.income == 1500
        expectedCalculateTaxes.costs == 1657.5
        expectedCalculateTaxes.incomeMinusCosts == -157.5
        expectedCalculateTaxes.pensionInsurance == 514.57
        expectedCalculateTaxes.incomeMinusCostsMinusPensionInsurance == -672.07
        expectedCalculateTaxes.incomeMinusCostsMinusPensionInsuranceRounded == -672
        expectedCalculateTaxes.incomeTax == -127.68
        expectedCalculateTaxes.healthInsurancePaid == 319.94
        expectedCalculateTaxes.healthInsuranceToSubtract == 275.5
        expectedCalculateTaxes.incomeTaxMinusHealthInsurance == -403.18
        expectedCalculateTaxes.finalIncomeTax == -403
        expectedCalculateTaxes.incomingVat == 315
        expectedCalculateTaxes.outgoingVat == 157.5
        expectedCalculateTaxes.vatToPay == 157.5
    }
}

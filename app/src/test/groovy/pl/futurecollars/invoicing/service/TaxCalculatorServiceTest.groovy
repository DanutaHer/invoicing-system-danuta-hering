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
        def actualIncome = taxService.income(invoiceIdentificationNumber)

        then:
        actualIncome == 1500
    }

    def "shouldGetCosts"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber
        database.getAll() >> List.of(invoice)

        when:
        def actualCosts = taxService.costs(invoiceIdentificationNumber)

        then:
        actualCosts == 1657.5
    }

    def "shouldGetIncomingVat"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber
        database.getAll() >> List.of(invoice)

        when:
        def actualIncomingVat = taxService.incomingVat(invoiceIdentificationNumber)

        then:
        actualIncomingVat == 315
    }

    def "shouldGetOutgoingVat"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber
        database.getAll() >> List.of(invoice)

        when:
        def actualOutgoingVat = taxService.outgoingVat(invoiceIdentificationNumber)

        then:
        actualOutgoingVat == 157.5
    }

    def "shouldGetEarnings"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber
        database.getAll() >> List.of(invoice)

        when:
        def actualEarnings = taxService.getEarnings(invoiceIdentificationNumber)

        then:
        actualEarnings == -157.5
    }

    def "shouldGetVatToPay"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceIdentificationNumber = invoice.getSeller().taxIdentificationNumber
        database.getAll() >> List.of(invoice)

        when:
        def actualVatToPay = taxService.getVatToPay(invoiceIdentificationNumber)

        then:
        actualVatToPay == 157.5
    }

    def "shouldCalculateTaxes"() {
        given:
        def invoice = TestHelper.invoice(1)
        def company = invoice.getSeller()
        database.getAll() >> List.of(invoice)

        when:
        def actualCalculateTaxes = taxService.calculateTaxes(company)

        then:
        actualCalculateTaxes.income == 1500
        actualCalculateTaxes.costs == 1657.5
        actualCalculateTaxes.incomeMinusCosts == -157.5
        actualCalculateTaxes.pensionInsurance == 514.57
        actualCalculateTaxes.incomeMinusCostsMinusPensionInsurance == -672.07
        actualCalculateTaxes.incomeMinusCostsMinusPensionInsuranceRounded == -672
        actualCalculateTaxes.incomeTax == -127.68
        actualCalculateTaxes.healthInsurancePaid == 319.94
        actualCalculateTaxes.healthInsuranceToSubtract == 275.5
        actualCalculateTaxes.incomeTaxMinusHealthInsurance == -403.18
        actualCalculateTaxes.finalIncomeTax == -403
        actualCalculateTaxes.incomingVat == 315
        actualCalculateTaxes.outgoingVat == 157.5
        actualCalculateTaxes.vatToPay == 157.5
    }
}

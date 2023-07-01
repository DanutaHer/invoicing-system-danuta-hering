package pl.futurecollarc.invoicing.db.memory

import pl.futurecollarc.invoicing.model.Company
import pl.futurecollarc.invoicing.model.Invoice
import pl.futurecollarc.invoicing.model.InvoiceEntry
import pl.futurecollarc.invoicing.model.Vat
import spock.lang.Specification

import java.time.LocalDate

class InMemoryDatabaseTest extends Specification {

    private InvoiceEntry invoiceEntry1 = new InvoiceEntry("desc", BigDecimal.ONE, BigDecimal.ONE, Vat.VAT_8)
    private Company companyBuyer = new Company("buyer", "ulica1", "123654")
    private Company companySeller = new Company("seller", "ulica1", "123654")
    private List<InvoiceEntry> inv = List.of(invoiceEntry1)
    private def date = LocalDate.now()
    private def firstInvoice = new Invoice(1, date, companyBuyer, companySeller, inv)
    private def secondInvoice = new Invoice(2, date, companyBuyer, companySeller, inv)
    private def thirdInvoice = new Invoice(3, date, companyBuyer, companySeller, inv)

    def "shouldSaveInvoice"() {
        given:
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase()

        and:
        inMemoryDatabase.save(firstInvoice)
        inMemoryDatabase.save(secondInvoice)

        expect:
        inMemoryDatabase.getMemoryDatabase().size() == 2
    }

    def "shouldGetInvoiceByID"() {
        given:
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase()

        and:
        inMemoryDatabase.save(firstInvoice)
        inMemoryDatabase.save(secondInvoice)

        expect:
        inMemoryDatabase.getByID(1) == Optional.ofNullable(firstInvoice)
    }

    def "shouldGetAll"() {
        given:
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase()
        List<Invoice> allInvoices = List.of(firstInvoice, secondInvoice)

        and:
        inMemoryDatabase.save(firstInvoice)
        inMemoryDatabase.save(secondInvoice)

        expect:
        inMemoryDatabase.getAll() == allInvoices
    }

    def "shouldUpdateInvoiceInDataBase"() {
        given:
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase()

        and:
        inMemoryDatabase.save(firstInvoice)
        inMemoryDatabase.save(secondInvoice)
        inMemoryDatabase.update(1, thirdInvoice)

        expect:
        inMemoryDatabase.getMemoryDatabase().size() == 2
        inMemoryDatabase.getByID(1) == Optional.ofNullable(thirdInvoice)
    }

    def "shouldDeleteInvoice"() {
        given:
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase()

        and:
        inMemoryDatabase.save(firstInvoice)
        inMemoryDatabase.save(secondInvoice)
        inMemoryDatabase.delete(1)

        expect:
        inMemoryDatabase.getMemoryDatabase().size() == 1
    }

    def "shouldThrowIllegalArgumentExceptionFor_GetByIDMethod"() {
        given:
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase()

        when:
        inMemoryDatabase.getByID(0)

        then:
        thrown(IllegalArgumentException)
    }

    def "shouldThrowIllegalArgumentExceptionFor_UpdateMethod"() {
        given:
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase()

        when:
        inMemoryDatabase.update(-2, thirdInvoice)

        then:
        thrown(IllegalArgumentException)
    }

    def "shouldThrowIllegalArgumentExceptionFor_DeleteMethod"() {
        given:
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase()

        when:
        inMemoryDatabase.delete(-3)

        then:
        thrown(IllegalArgumentException)
    }
}

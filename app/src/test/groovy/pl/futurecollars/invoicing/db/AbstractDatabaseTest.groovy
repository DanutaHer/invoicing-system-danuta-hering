package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification


abstract class AbstractDatabaseTest extends Specification {

    abstract Database<Invoice> getDatabaseInstance()

    Database<Invoice> database

    def setup() {
        database = getDatabaseInstance()
        database.getAll().forEach(invoice -> database.delete(invoice.getId()))
        assert database.getAll().isEmpty()
    }

    def "shouldSaveInvoice"() {
        given:
        def savedInvoiceId = database.save(TestHelper.invoice(1))

        when:
        def expectedInvoice = TestHelper.invoice(savedInvoiceId)
        expectedInvoice.setId(savedInvoiceId)
        def actualInvoice = database.getByID(savedInvoiceId).get()
        resetIds(actualInvoice)

        then:
        actualInvoice.toString() == expectedInvoice.toString()
    }

    def "shouldGetAll"() {
        given:
        database.save(TestHelper.invoice(1))
        def savedInvoice2Id = database.save(TestHelper.invoice(2))

        when:
        def actualInvoice = database.getByID(savedInvoice2Id).get()
        resetIds(actualInvoice)

        def allInvoices = database.getAll()

        then:
        allInvoices.size() == 2
        actualInvoice.getNumber() == TestHelper.invoice(1).getNumber()
    }

    def "shouldGetInvoiceByID"() {
        given:
        database.save(TestHelper.invoice(1))
        def savedInvoiceId = database.save(TestHelper.invoice(2))

        when:
        def actualInvoice = database.getByID(savedInvoiceId).get()
        resetIds(actualInvoice)
        def expectedInvoice = TestHelper.invoice(savedInvoiceId)
        expectedInvoice.setId(savedInvoiceId)

        then:
        actualInvoice.toString() == resetIds(expectedInvoice).toString()
    }

    def "shouldUpdateInvoiceInDataBase"() {
        given:
        database.save(TestHelper.invoice(1))
        def savedInvoiceId = database.save(TestHelper.invoice(2))
        def invoiceToUpdate = TestHelper.invoice(3)
        invoiceToUpdate.setNumber("123/4581/00004444")

        when:
        database.update(savedInvoiceId, invoiceToUpdate)

        def expectedInvoice = database.getByID(savedInvoiceId).get()
        resetIds(expectedInvoice)
        resetIds(invoiceToUpdate)
        invoiceToUpdate.setId(savedInvoiceId)

        then:
        expectedInvoice.toString() == invoiceToUpdate.toString()
        database.getByID(savedInvoiceId).get().getNumber() == "123/4581/00004444"
    }

    def "shouldDeleteInvoice"() {
        given:
        def savedInvoiceId = database.save(TestHelper.invoice(2))

        when:
        database.delete(savedInvoiceId)

        then:
        database.getByID(savedInvoiceId).isEmpty()
    }

    private static Invoice resetIds(Invoice invoice) {
        invoice.getBuyer().id = 0
        invoice.getSeller().id = 0
        invoice.entries.forEach {
            it.id = 0
            it.getExpenseRelatedToCar().id = 0
        }
        invoice
    }
}

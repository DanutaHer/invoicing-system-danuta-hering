package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

abstract class AbstractDatabaseTest extends Specification {

    Database database

    abstract Database getDatabaseInstance()

    def setup() {
        database = getDatabaseInstance()
        database.getAll().each {invoice -> database.delete(invoice.id)}

        assert database.getAll().isEmpty()
    }

    def "shouldSaveInvoice"() {
        when:
        database.save(TestHelper.invoice(1))
        then:
        database.getByID(1).get() == TestHelper.invoice(1)
    }

    def "shouldGetInvoiceByID"() {
        when:
        database.save(TestHelper.invoice(1))
        database.save(TestHelper.invoice(2))

        then:
        database.getByID(2).get() == TestHelper.invoice(2)
    }

    def "shouldGetAll"() {
        when:
        database.save(TestHelper.invoice(1))
        database.save(TestHelper.invoice(2))
        List<Invoice> expectedInvoiceList = database.getAll()

        then:
        expectedInvoiceList.size() == 2
        database.getByID(1).get() == TestHelper.invoice(1)
    }

    def "shouldUpdateInvoiceInDataBase"() {
        given:
        def invoiceToUpdate = TestHelper.invoice(1)
        invoiceToUpdate.setNumber("123/458/00004444")

        when:
        database.save(TestHelper.invoice(1))
        database.update(1, invoiceToUpdate)
        def expectedInvoice = database.getByID(1).get()

        then:
        expectedInvoice == invoiceToUpdate
    }

    def "shouldDeleteInvoice"() {
        when:
        database.delete(1)

        then:
        !database.getByID(1).isPresent()
    }
}

package pl.futurecollars.invoicing.db

import com.mongodb.client.MongoDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

abstract class AbstractDatabaseTest extends Specification {

    abstract Database getDatabaseInstance()

    Database database

    @Autowired
    private ApplicationContext context

    def setup() {
        database = getDatabaseInstance()
        database.getAll().forEach(invoice -> database.delete(invoice.getId()))
        assert database.getAll().isEmpty()
    }

    def "shouldSaveInvoice"() {
        when:
        database.save(TestHelper.invoice(1))

        def expectedInvoice = TestHelper.invoice(1)
        def actualInvoice = database.getByID(1).get()
        resetIds(actualInvoice)

        then:
        actualInvoice.toString() == expectedInvoice.toString()
    }

    def "shouldGetAll"() {
        when:
        database.save(TestHelper.invoice(1))
        database.save(TestHelper.invoice(2))
        database.save(TestHelper.invoice(3))
        List<Invoice> expectedInvoiceList = ArrayList.of(TestHelper.invoice(1), TestHelper.invoice(2), TestHelper.invoice(3))

        def actualInvoice = database.getByID(2).get()
        resetIds(actualInvoice)

        then:
        expectedInvoiceList.size() == 3
        actualInvoice.toString() == resetIds(expectedInvoiceList.get(1)).toString()
    }

    def "shouldGetInvoiceByID"() {
        when:
        database.save(TestHelper.invoice(1))
        database.save(TestHelper.invoice(2))

        def actualInvoice = database.getByID(2).get()
        resetIds(actualInvoice)

        then:
        actualInvoice.toString() == resetIds(TestHelper.invoice(2)).toString()
    }

    def "shouldUpdateInvoiceInDataBase"() {
        given:
        database.save(TestHelper.invoice(1))
        database.save(TestHelper.invoice(2))
        def invoiceToUpdate = TestHelper.invoice(3)
        invoiceToUpdate.setNumber("123/4581/00004444")

        when:
        database.update(2, invoiceToUpdate)

        def expectedInvoice = database.getByID(2).get()
        resetIds(expectedInvoice)
        resetIds(invoiceToUpdate)
        invoiceToUpdate.setId(2)

        then:
        expectedInvoice.toString() == invoiceToUpdate.toString()
        database.getByID(2).get().getNumber() == "123/4581/00004444"
    }

    def "shouldDeleteInvoice"() {
        when:
        database.delete(1)

        then:
        !database.getByID(1).isPresent()
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

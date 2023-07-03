package pl.futurecollarc.invoicing.db

import pl.futurecollarc.invoicing.TestHelper
import pl.futurecollarc.invoicing.model.Invoice
import spock.lang.Specification

abstract class AbstractDatabaseTest extends Specification {

    Database database = getDatabaseInstance()

    abstract Database getDatabaseInstance()

    private List<Invoice> allInvoices = List.of(TestHelper.invoice(0), TestHelper.invoice(1), TestHelper.invoice(2))

    def setup() {
        database.save(TestHelper.invoice(0))
        database.save(TestHelper.invoice(1))
        database.save(TestHelper.invoice(2))
    }

    def "shouldSaveInvoice"() {
        expect:
        database.getByID(0) == Optional.of(TestHelper.invoice(0))
        database.getByID(1) == Optional.of(TestHelper.invoice(1))
    }

    def "shouldGetInvoiceByID"() {
        expect:
        database.getByID(1) == Optional.ofNullable(TestHelper.invoice(1))
    }

    def "shouldGetAll"() {
        expect:
        database.getAll() == (allInvoices)
    }

    def "shouldUpdateInvoiceInDataBase"() {
        when:
        database.update(1, TestHelper.invoice(4))

        then:
        database.getByID(1) == Optional.of(TestHelper.invoice(4))
    }

    def "shouldDeleteInvoice"() {
        when:
        database.delete(0)

        then:
        database.getByID(0) == Optional.empty()
    }

    def "shouldThrowIllegalArgumentExceptionFor_GetByIDMethod"() {
        when:
        database.getByID(-2)

        then:
        thrown(IllegalArgumentException)
    }

    def "shouldThrowIllegalArgumentExceptionFor_UpdateMethod"() {
        when:
        database.update(-2, TestHelper.invoice(4))

        then:
        thrown(IllegalArgumentException)
    }

    def "shouldThrowIllegalArgumentExceptionFor_DeleteMethod"() {
        when:
        database.delete(-3)

        then:
        thrown(IllegalArgumentException)
    }
}

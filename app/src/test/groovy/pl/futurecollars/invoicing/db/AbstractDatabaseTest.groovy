package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.TestHelper
import spock.lang.Specification

abstract class AbstractDatabaseTest extends Specification {

    Database database = getDatabaseInstance()

    abstract Database getDatabaseInstance()

    def setup() {
        database.save(TestHelper.invoice(1))
        database.save(TestHelper.invoice(2))
        database.save(TestHelper.invoice(3))
        database.save(TestHelper.invoice(5))
    }

    def "shouldSaveInvoice"() {

        expect:
        database.getByID(2) == Optional.ofNullable(TestHelper.invoice(2))
    }

    def "shouldGetInvoiceByID"() {
        expect:
        database.getByID(2) == Optional.ofNullable(TestHelper.invoice(2))
    }

    def "shouldGetAll"() {
        expect:
        !database.getAll().isEmpty()
    }

    def "shouldUpdateInvoiceInDataBase"() {
        when:
        database.update(1, TestHelper.invoice(4))

        then:
        database.getByID(1).isPresent()
    }

    def "shouldDeleteInvoice"() {
        when:
        database.delete(1)

        then:
        !database.getByID(1).isPresent()
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

    def "shouldThrowOutOfBoundsExceptionFor_UpdateMethod"() {
        when:
        database.delete(1000000)

        then:
        thrown(IndexOutOfBoundsException)
    }

    def "shouldThrowOutOfBoundsExceptionFor_DeleteMethod"() {
        when:
        database.delete(1000000)

        then:
        thrown(IndexOutOfBoundsException)
    }
}

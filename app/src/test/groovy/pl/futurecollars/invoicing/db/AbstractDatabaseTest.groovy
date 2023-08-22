package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.TestHelper
import spock.lang.Specification

abstract class AbstractDatabaseTest extends Specification {

    Database database = getDatabaseInstance()

    abstract Database getDatabaseInstance()

//    def setup() {
//        database = getDatabaseInstance()
////        database.reset()
//
//        assert database.getAll().isEmpty()
//    }

    def "shouldSaveInvoice"() {
        when:
        database.save(TestHelper.invoice(1))
        then:
        database.getByID(1) == Optional.ofNullable(TestHelper.invoice(1))
    }

    def "shouldGetInvoiceByID"() {
        when:
        database.save(TestHelper.invoice(1))
        database.save(TestHelper.invoice(2))

        then:
        database.getByID(2) == Optional.ofNullable(TestHelper.invoice(2))
    }

    def "shouldGetAll"() {
        when:
        database.save(TestHelper.invoice(1))
        database.save(TestHelper.invoice(2))

        then:
        !database.getAll().isEmpty()
    }

    def "shouldUpdateInvoiceInDataBase"() {
        given:
        database.save(TestHelper.invoice(1))

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
}

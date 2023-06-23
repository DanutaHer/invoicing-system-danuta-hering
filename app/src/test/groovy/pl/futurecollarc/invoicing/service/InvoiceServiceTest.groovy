package pl.futurecollarc.invoicing.service

import pl.futurecollarc.invoicing.db.Database
import pl.futurecollarc.invoicing.model.Invoice
import spock.lang.Specification

class InvoiceServiceTest extends Specification {

    private Database database = Mock()
    private InvoiceService service = new InvoiceService(database);

    def "shouldSave"() {
        given:
        def invoice = new Invoice()

        when:
        service.save(invoice)

        then:
        1 * database.save(invoice)
    }

    def "shouldGetByID"() {
        when:
        service.getByID(2)

        then:
        1 * database.getByID(2)
    }

    def "shouldGetAll"() {
        when:
        service.getAll()

        then:
        1 * database.getAll()
    }

    def "shouldUpdate"() {
        given:
        def invoice = new Invoice()

        when:
        service.update(2, invoice)

        then:
        1 * database.update(2, invoice)
    }

    def "shouldDelete"() {
        when:
        service.delete(2)

        then:
        1 * database.delete(2)

    }
}

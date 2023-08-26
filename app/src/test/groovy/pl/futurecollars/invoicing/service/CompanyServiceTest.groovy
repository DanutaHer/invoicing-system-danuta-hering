package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

class CompanyServiceTest extends Specification {

    private Database<Company> database = Mock()
    private CompanyService service = new CompanyService(database)

    def "shouldSave"() {
        given:
        def company = new Company()

        when:
        service.save(company)

        then:
        1 * database.save(company)
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
        def company = new Company()

        when:
        service.update(2, company)

        then:
        1 * database.update(2, company)
    }

    def "shouldDelete"() {
        when:
        service.delete(2)

        then:
        1 * database.delete(2)

    }
}

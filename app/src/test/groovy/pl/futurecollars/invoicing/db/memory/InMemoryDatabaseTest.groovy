package pl.futurecollars.invoicing.db.memory

import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database

@IfProfileValue(name = "spring.profiles.active", value = "memory")
class InMemoryDatabaseTest extends AbstractDatabaseTest {

    @Override
    Database getDatabaseInstance() {
        return new InMemoryDatabase()
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

package pl.futurecollarc.invoicing.db.memory


import pl.futurecollarc.invoicing.db.AbstractDatabaseTest
import pl.futurecollarc.invoicing.db.Database

class InMemoryDatabaseTest extends AbstractDatabaseTest {

    @Override
    Database getDatabaseInstance() {
        return new InMemoryDatabase()
    }
}

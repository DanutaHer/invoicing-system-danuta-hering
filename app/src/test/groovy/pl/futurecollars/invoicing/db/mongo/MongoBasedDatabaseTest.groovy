package pl.futurecollars.invoicing.db.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice

@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "mongo")
class MongoBasedDatabaseTest extends AbstractDatabaseTest {

    @Autowired
    private Database<Invoice> mongoBasedDatabase

    @Override
    Database<Invoice> getDatabaseInstance() {
        assert mongoBasedDatabase != null
        mongoBasedDatabase
    }
}

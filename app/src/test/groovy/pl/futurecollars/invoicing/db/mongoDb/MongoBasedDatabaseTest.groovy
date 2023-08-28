package pl.futurecollars.invoicing.db.mongoDb

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database

@SpringBootTest
//@IfProfileValue(name = "spring.profiles.active", value = "mongo")
class MongoBasedDatabaseTest extends AbstractDatabaseTest {

    @Autowired
    private MongoBasedDatabase mongoBasedDatabase

    @Override
    Database getDatabaseInstance() {
        assert mongoBasedDatabase != null
        mongoBasedDatabase
    }
}

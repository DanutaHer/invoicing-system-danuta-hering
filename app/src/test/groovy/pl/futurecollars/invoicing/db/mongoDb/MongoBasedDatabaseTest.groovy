package pl.futurecollars.invoicing.db.mongoDb

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.IfProfileValue
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database

@SpringBootTest
class MongoBasedDatabaseTest extends AbstractDatabaseTest {

    @Autowired
    private Database mongoBasedDatabase

    @Override
    Database getDatabaseInstance() {
        assert mongoBasedDatabase != null
        mongoBasedDatabase
    }
}

package pl.futurecollars.invoicing.db.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JpaDatabaseTest extends AbstractDatabaseTest {

    @Autowired
    private InvoiceRepository invoiceRepository

    @Override
    Database getDatabaseInstance() {
        assert invoiceRepository != null
        new JpaDatabase(invoiceRepository)
    }

}

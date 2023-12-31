package pl.futurecollars.invoicing.db.files

import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.FilesService
import pl.futurecollars.invoicing.service.JsonService

import java.nio.file.Path

@IfProfileValue(name = "spring.profiles.active", value = "file")
class FileDatabaseTest extends AbstractDatabaseTest {

    @Override
    Database<Invoice> getDatabaseInstance() {

        JsonService jsonService = new JsonService()
        FilesService filesService = new FilesService()
        Path pathToId = File.createTempFile('TemporaryId', '.txt').toPath()
        Path pathToInvoices = File.createTempFile('invoices', '.txt').toPath()
        IdService idService = new IdService(pathToId, filesService)

        return new FileDatabase<>(filesService, jsonService, idService, pathToInvoices, Invoice)
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

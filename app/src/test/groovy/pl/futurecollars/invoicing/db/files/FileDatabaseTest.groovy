package pl.futurecollars.invoicing.db.files

import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.service.FilesService
import pl.futurecollars.invoicing.service.JsonService

import java.nio.file.Path

class FileDatabaseTest extends AbstractDatabaseTest {

    Database database = getDatabaseInstance()

    @Override
    Database getDatabaseInstance() {

        JsonService jsonService = new JsonService()
        FilesService filesService = new FilesService()
        Path pathToId = File.createTempFile('TemporaryId', '.txt').toPath()
        Path pathToInvoices = File.createTempFile('invoices', '.txt').toPath()
        IdService idService = new IdService(pathToId, filesService)

        return new FileDatabase(filesService, jsonService, idService, pathToInvoices)
    }

    def "shouldThrowRuntimeExceptionFor_UpdateMethod"() {
        when:
        database.update(1000000, TestHelper.invoice(4))

        then:
        thrown(RuntimeException)
    }
}

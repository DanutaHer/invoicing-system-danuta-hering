package pl.futurecollarc.invoicing.db.files

import pl.futurecollarc.invoicing.db.AbstractDatabaseTest
import pl.futurecollarc.invoicing.db.Database
import pl.futurecollarc.invoicing.service.FilesService
import pl.futurecollarc.invoicing.service.JsonService

import java.nio.file.Path

class FileDatabaseTest extends AbstractDatabaseTest {

    @Override
    Database getDatabaseInstance() {

        JsonService jsonService = new JsonService()
        FilesService filesService = new FilesService()
        Path pathToId = File.createTempFile('TemporaryId', '.txt').toPath()
        Path pathToInvoices = File.createTempFile('invoices', '.txt').toPath()
        IdService idService = new IdService(pathToId, filesService)

        return new FileDatabase(filesService, jsonService, idService, pathToInvoices)
    }
}

package pl.futurecollarc.invoicing.db.files

import pl.futurecollarc.invoicing.config.Config
import pl.futurecollarc.invoicing.db.AbstractDatabaseTest
import pl.futurecollarc.invoicing.db.Database
import pl.futurecollarc.invoicing.service.FilesService
import pl.futurecollarc.invoicing.service.JsonService

class FileDatabaseTest extends AbstractDatabaseTest {

    @Override
    Database getDatabaseInstance() {

        JsonService jsonService = new JsonService()
        FilesService filesService = new FilesService()
        IdService idService = new IdService(filesService, Config.ID_FILE_LOCATION)

        return new FileDatabase(filesService, jsonService, idService)
    }
}

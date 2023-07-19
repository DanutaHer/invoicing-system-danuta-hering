package pl.futurecollars.invoicing.db.files


import pl.futurecollars.invoicing.service.FilesService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class IdServiceTest extends Specification {

    FilesService filesService = new FilesService()
    Path path = File.createTempFile('TemporaryId', '.txt').toPath()

    def "shouldGetNextIdAndIncreament"() {
        given:
        Files.writeString(path as Path, "3")
        IdService idService = new IdService(path, filesService)

        expect:
        idService.getNextIdAndIncreament()
        ['4'] == Files.readAllLines(path)
    }
}

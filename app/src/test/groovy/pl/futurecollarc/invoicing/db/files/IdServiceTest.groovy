package pl.futurecollarc.invoicing.db.files

import pl.futurecollarc.invoicing.service.FilesService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class IdServiceTest extends Specification {

    FilesService filesService = new FilesService()
    private Path path = File.createTempFile('TemporaryId', '.txt').toPath()

    def "shouldGetNextIdAndIncreament"() {
        given:
        Files.writeString(path as Path, "3")
        IdService idService = new IdService(filesService, path as String)

        expect:
        idService.getNextIdAndIncreament(path as String)
        ['4'] == Files.readAllLines(path)
    }
}

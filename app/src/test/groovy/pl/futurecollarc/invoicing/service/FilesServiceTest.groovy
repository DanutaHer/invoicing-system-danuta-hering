package pl.futurecollarc.invoicing.service


import spock.lang.Specification

import java.nio.file.Path

class FilesServiceTest extends Specification {

    FilesService filesService = new FilesService()
    List<String> invoicesExample = new ArrayList<>()
    Path path = File.createTempFile('TemporaryFile', '.txt').toPath()
    String line = ""

    def setup() {
        invoicesExample.add("Invoice1")
        invoicesExample.add("Invoice2")
    }

    def "shouldWriteListToFile"() {
        expect:
        filesService.appendLineToFile(path, line)
    }

    def "shouldReadInvoicesFromFile"() {
        expect:
        filesService.writeLinesToFile(path, invoicesExample)
    }

    def "shouldWriteTextToFile"() {
        expect:
        filesService.writeTextTo(path, "Example String")
    }

    def "shouldREadTextFromFile"() {
        given:
        filesService.writeTextTo(path, "Example String")

        expect:
        filesService.readAllLines(path) == (List.of("Example String"))
    }
}

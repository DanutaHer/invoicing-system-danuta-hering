package pl.futurecollarc.invoicing.service


import spock.lang.Specification

class FilesServiceTest extends Specification {

    FilesService filesService = new FilesService()
    List<String> invoicesExample = new ArrayList<>()
    String line = ""

    def setup() {
        invoicesExample.add("Invoice1")
        invoicesExample.add("Invoice2")
    }

    def "shouldWriteListToFile"() {
        expect:
        filesService.appendLineToFile("temporaryFileToAppendLine.txt", line)
    }

    def "shouldReadInvoicesFromFile"() {
        expect:
        filesService.writeLinesToFile("temporaryFileToWriteLines.txt", invoicesExample)
    }

    def "shouldWriteTextToFile"() {
        expect:
        filesService.writeTextTo("temporaryFileToTest.txt", "Example String")
    }

    def "shouldREadTextFromFile"() {
        expect:
        filesService.readAllLines("temporaryFileToTest.txt") == (List.of("Example String"))
    }
}

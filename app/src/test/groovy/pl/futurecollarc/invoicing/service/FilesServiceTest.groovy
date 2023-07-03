package pl.futurecollarc.invoicing.service


import spock.lang.Specification

class FilesServiceTest extends Specification {

    FilesService filesService = new FilesService()
    List<String> invoicesExample = new ArrayList<>()

    def setup() {
        invoicesExample.add("Invoice1")
        invoicesExample.add("Invoice2")
    }

    def "shouldWriteListToFile"() {
        expect:
        filesService.writeInvoicesTo("temporaryFile", invoicesExample)
    }

    def "shouldReadInvoicesFromFile"() {
        expect:
        filesService.readInvoicesFrom("temporaryFile") equals(invoicesExample)
    }

    def "shouldWriteTextToFile"() {
        expect:
        filesService.writeTextTo("temporaryFileToTest", "Example String")
    }
}

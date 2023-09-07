package pl.futurecollars.invoicing.controllers.invoice

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.InvoiceService
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Specification

import java.time.LocalDate


@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Autowired
    private InvoiceService invoiceService

    def "should return empty array when no invoices were created"() {
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/invoices"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        result == "[]"
    }

    def "should add invoice to id 1"() {
        given:
        def invoiceId = invoiceService.save(TestHelper.invoice(1))
        def invoiceJson = jsonService.objectToJson(TestHelper.invoice(1))

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/invoices")
                .content(invoiceJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        result == (invoiceId + 1).toString()

        cleanup:
        cleanup()
    }

    def "should get all invoices"() {
        given:
        invoiceService.save(TestHelper.invoice(1))
        invoiceService.save(TestHelper.invoice(2))

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/invoices"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        def result = jsonService.jsonToObject(resultJson, Invoice[])
        result.size() == 2

        cleanup:
        cleanup()
    }

    def "should get invoice from the id 1"() {
        given:
        def invoice1 = TestHelper.invoice(1)
        def invoiceId = invoiceService.save(invoice1)

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/invoices/$invoiceId"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        def result = jsonService.jsonToObject(resultJson, Invoice)
        result.number == invoice1.number

        cleanup:
        cleanup()
    }

    def "should get response 404 - not found when get nonexistent invoice from id 100"() {
        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/invoices/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "should update invoice from id 1 to invoice from id 3"() {
        given:
        invoiceService.save(TestHelper.invoice(1))
        def invoice3 = TestHelper.invoice(3)
        invoice3.setNumber("12345679")
        def invoice3Json = jsonService.objectToJson(invoice3)
        def invoice3Id = invoiceService.save(invoice3)

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.put("/invoices/$invoice3Id")
                .content(invoice3Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        def result = jsonService.jsonToObject(resultJson, Invoice)
        result.getNumber() == invoice3.getNumber()

        cleanup:
        cleanup()
    }

    def "should get response 404 - not found when update nonexistent invoice from id 100"() { //return 400?????????????????
        given:
        def invoice3 = TestHelper.invoice(3)
        def invoice3Json = jsonService.objectToJson(invoice3)

        expect:
        mockMvc.perform(MockMvcRequestBuilders.put("/invoices/100")
                .content(invoice3Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "should delete invoice from id 1"() {
        given:
        def invoice1 = TestHelper.invoice(1)
        def invoiceId = invoiceService.save(invoice1)

        when:
        mockMvc.perform(MockMvcRequestBuilders.delete("/invoices/$invoiceId"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        invoiceService.getByID(invoiceId).isEmpty()

        cleanup:
        cleanup()
    }

    def "should get response 404 - not found when delete nonexistent invoice from id 100"() {
        expect:
        mockMvc.perform(MockMvcRequestBuilders.delete("/invoices/100"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
    }

    def cleanup() {
        invoiceService.getAll().each(i -> invoiceService.delete(i.id))
    }
}

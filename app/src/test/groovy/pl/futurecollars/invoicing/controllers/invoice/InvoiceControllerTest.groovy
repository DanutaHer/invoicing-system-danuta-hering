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


    @Autowired Flyway flyway
    def setup() { flyway.clean(); flyway.migrate(); }

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
        def invoice = TestHelper.invoice(1)
        def invoiceJson = jsonService.objectToJson(invoice)

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/invoices")
                .content(invoiceJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        Integer.valueOf(result) == 1
        invoiceService.getByID(1) == Optional.of(invoice)
    }

    def "should get all invoices"() {
        given:
        def invoice1 = TestHelper.invoice(3)
        invoiceService.save(invoice1)

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/invoices"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        def result = jsonService.jsonToObject(resultJson, Invoice[])
        result == invoiceService.getAll()
    }

    def "should get invoice from the id 1"() {
        given:
        def invoice1 = TestHelper.invoice(1)
        invoiceService.save(invoice1)

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/invoices/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        def result = jsonService.jsonToObject(resultJson, Invoice)
        Optional.of(result) == invoiceService.getByID(1)
    }

    def "should get response 404 - not found when get nonexistent invoice from id 100"() {
        expect:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/invoices/100"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
    }

    def "should update invoice from id 1 to invoice from id 3"() {
        given:
        def invoice1 = TestHelper.invoice(1)
        def invoice3 = TestHelper.invoice(3)
        def actualDate = LocalDate.now().plusDays(10)
        invoice3.setDate(actualDate)
        def invoice3Json = jsonService.objectToJson(invoice3)
        invoiceService.save(invoice1)

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.put("/invoices/1")
                .content(invoice3Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        def result = jsonService.jsonToObject(resultJson, Invoice)
        result.getDate() == actualDate
    }

    def "should get response 404 - not found when update nonexistent invoice from id 100"() {
        expect:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.put("/invoices/100"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
    }

    def "should delete invoice from id 1"() {
        given:
        def invoice1 = TestHelper.invoice(1)
        invoiceService.save(invoice1)

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.delete("/invoices/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        invoiceService.getByID(1).isEmpty()
    }

    def "should get response 404 - not found when delete nonexistent invoice from id 100"() {
        expect:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.delete("/invoices/100"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
    }
}

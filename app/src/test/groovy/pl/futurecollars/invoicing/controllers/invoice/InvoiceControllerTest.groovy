package pl.futurecollars.invoicing.controllers.invoice

import com.mongodb.client.MongoDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.InvoiceService
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Requires
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Autowired
    private InvoiceService invoiceService

    @Autowired
    private ApplicationContext context

    @Requires({ System.getProperty('spring.profiles.active', 'memory').contains("mongo") })
    def "database is dropped to ensure clean state"() {
        expect:
        MongoDatabase mongoDatabase = context.getBean(MongoDatabase)
        mongoDatabase.drop()
    }

    def "should return empty array when no invoices were created"() {
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/invoices").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        result == "[]"
    }

    def "should add invoice to database"() {
        given:
        def invoiceId = invoiceService.save(TestHelper.invoice(1))
        def invoiceJson = jsonService.objectToJson(TestHelper.invoice(1))

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/invoices")
                .with(csrf())
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
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/invoices").with(csrf()))
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

    def "should get exact invoice from database"() {
        given:
        def invoice1 = TestHelper.invoice(1)
        def invoiceId = invoiceService.save(invoice1)

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/invoices/$invoiceId").with(csrf()))
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
        mockMvc.perform(MockMvcRequestBuilders.get("/invoices/100").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "should update invoice number"() {
        given:
        invoiceService.save(TestHelper.invoice(1))
        def invoice3 = TestHelper.invoice(3)
        invoice3.setNumber("12345679")
        def invoice3Json = jsonService.objectToJson(invoice3)
        def invoice3Id = invoiceService.save(invoice3)

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.put("/invoices/$invoice3Id")
                .with(csrf())
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

    def "should get response 404 - not found when update nonexistent invoice from id 100"() {
        given:
        def invoice3 = TestHelper.invoice(3)
        def invoice3Json = jsonService.objectToJson(invoice3)

        expect:
        mockMvc.perform(MockMvcRequestBuilders.put("/invoices/100")
                .with(csrf())
                .content(invoice3Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "should delete exact invoice"() {
        given:
        def invoice1 = TestHelper.invoice(1)
        def invoiceId = invoiceService.save(invoice1)

        when:
        mockMvc.perform(MockMvcRequestBuilders.delete("/invoices/$invoiceId").with(csrf()))
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
        mockMvc.perform(MockMvcRequestBuilders.delete("/invoices/100").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def cleanup() {
        invoiceService.getAll().each(i -> invoiceService.delete(i.id))
    }
}

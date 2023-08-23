package pl.futurecollars.invoicing.controllers.tax

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.service.InvoiceService
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class TaxCalculatorControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Autowired
    private InvoiceService invoiceService

    def setup() {
        invoiceService.getAll().each { invoice -> invoiceService.delete(invoice.id) }
    }

    def "should get tax calculator"() {
        given:
        def company = TestHelper.invoice(1).getBuyer()
        def companyJson = jsonService.objectToJson(company)
        def taxCalculator = jsonService.objectToJson(TestHelper.taxCalculator())

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/invoices/taxCalculator")
                .content(companyJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        result == taxCalculator
    }

    def "should get response 404 - not found when no company specified"() {
        expect:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/invoices/taxCalculator/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
    }
}

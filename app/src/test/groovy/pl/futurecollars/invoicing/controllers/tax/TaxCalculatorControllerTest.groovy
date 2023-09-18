package pl.futurecollars.invoicing.controllers.tax

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.service.InvoiceService
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.TaxCalculatorService
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
class TaxCalculatorControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Autowired
    private TaxCalculatorService taxCalculatorService

    @Autowired
    private InvoiceService invoiceService

    def setup() {
        invoiceService.getAll().each { invoice -> invoiceService.delete(invoice.id) }
    }

    def "should get response 404 - not found when no value is given"() {
        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/invoices/taxCalculator/").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "should get zeros result when there are no this company in the system"() {
        when:
        def taxCalculatorResponse = taxCalculatorService.calculateTaxes(TestHelper.company(0))

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 0
        // vat
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToPay == 0
    }

    def "should get result when there are company in the system"() {
        given:
        def invoice = TestHelper.invoice(1)
        def invoiceJson = jsonService.objectToJson(invoice)
        mockMvc.perform(MockMvcRequestBuilders.post("/invoices")
                .with(csrf())
                .content(invoiceJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        when:
        def taxCalculatorResponse = taxCalculatorService.calculateTaxes(TestHelper.company(1))

        then:
        taxCalculatorResponse.income == 1500
        taxCalculatorResponse.costs == 1657.5
        taxCalculatorResponse.incomeMinusCosts == -157.5
        taxCalculatorResponse.pensionInsurance == 514.57
        taxCalculatorResponse.incomeMinusCostsMinusPensionInsurance == -672.07
        taxCalculatorResponse.incomeMinusCostsMinusPensionInsuranceRounded == -672
        taxCalculatorResponse.incomeTax == -127.68
        taxCalculatorResponse.healthInsurancePaid == 319.94
        taxCalculatorResponse.healthInsuranceToSubtract == 275.5
        taxCalculatorResponse.incomeTaxMinusHealthInsurance == -403.18
        taxCalculatorResponse.finalIncomeTax == -403
        // vat
        taxCalculatorResponse.incomingVat == 315
        taxCalculatorResponse.outgoingVat == 157.5
        taxCalculatorResponse.vatToPay == 157.5
    }
}

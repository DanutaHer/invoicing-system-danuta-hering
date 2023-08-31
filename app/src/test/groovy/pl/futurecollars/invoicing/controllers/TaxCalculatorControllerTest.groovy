package pl.futurecollars.invoicing.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.futurecollars.invoicing.TestHelper
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.TaxCalculatorService
import spock.lang.Specification

import java.math.RoundingMode

import static pl.futurecollars.invoicing.TestHelper.company

@SpringBootTest
@AutoConfigureMockMvc
class TaxCalculatorControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Autowired
    private TaxCalculatorService taxCalculatorService

    def "should get response 404 - not found when get nonexistent taxIdentificationNumber from id 1"() {
        expect:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/invoices/taxCalculator/"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "should get zeros returned when there are no this company in the system"() {
        given:

        when:
        def taxCalculatorResponse = taxCalculatorService.calculateTaxes(company(0))

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 0
//        taxCalculatorResponse.pensionInsurance == 0
//        taxCalculatorResponse.incomeMinusCostsMinusPensionInsurance == 0
//        taxCalculatorResponse.incomeMinusCostsMinusPensionInsuranceRounded == 0
//        taxCalculatorResponse.incomeTax == 0
//        taxCalculatorResponse.healthInsurancePaid == 0
//        taxCalculatorResponse.healthInsuranceToSubtract == 0
//        taxCalculatorResponse.incomeTaxMinusHealthInsurance == 0
//        taxCalculatorResponse.finalIncomeTax == 0
        // vat
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToPay == 0
    }
}

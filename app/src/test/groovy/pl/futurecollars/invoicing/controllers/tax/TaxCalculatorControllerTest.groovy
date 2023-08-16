package pl.futurecollars.invoicing.controllers.tax

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.TaxCalculatorService
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class TaxCalculatorControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Autowired
    private TaxCalculatorService taxCalculatorService

    def "should get response 404 - not found when no tax identification number was provided"() {
        expect:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/invoices/taxCalculator/"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
    }
}

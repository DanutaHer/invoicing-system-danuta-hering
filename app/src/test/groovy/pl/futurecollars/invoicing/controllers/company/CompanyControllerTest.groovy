package pl.futurecollars.invoicing.controllers.company

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
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.service.CompanyService
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Requires
import spock.lang.Specification

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Autowired
    private CompanyService companyService

    @Autowired
    private ApplicationContext context

    @Requires({ System.getProperty('spring.profiles.active', 'memory').contains("mongo") })
    def "database is dropped to ensure clean state"() {
        expect:
        MongoDatabase mongoDatabase = context.getBean(MongoDatabase)
        mongoDatabase.drop()
    }

    def "should return empty array when no companies were created"() {
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        result == "[]"
    }

    def "should add company to database"() {
        given:
        def companyId = companyService.save(TestHelper.company(1))
        def companyJson = jsonService.objectToJson(TestHelper.company(1))

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/companies")
                .content(companyJson)
                .contentType(MediaType.APPLICATION_JSON))
                .with(csrf())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        result == (companyId + 1).toString()

        cleanup:
        cleanup()
    }

    def "should get all companies"() {
        given:
        companyService.save(TestHelper.company(1))
        companyService.save(TestHelper.company(2))

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        def result = jsonService.jsonToObject(resultJson, Company[])
        result.size() == 2

        cleanup:
        cleanup()
    }

    def "should get exact company from database"() {
        given:
        def company1 = TestHelper.company(1)
        def companyId = companyService.save(company1)

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/companies/$companyId"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        def result = jsonService.jsonToObject(resultJson, Company)
        result.name == company1.name

        cleanup:
        cleanup()
    }

    def "should get response 404 - not found when get nonexistent company from id 100"() {
        expect:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/companies/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "should update company name"() {
        given:
        companyService.save(TestHelper.company(1))
        def company3 = TestHelper.company(3)
        company3.setName("Global LTD")
        def company3Json = jsonService.objectToJson(company3)
        def company3Id = companyService.save(company3)

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.put("/companies/$company3Id")
                .content(company3Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        def result = jsonService.jsonToObject(resultJson, Company)
        result.getName() == company3.getName()

        cleanup:
        cleanup()
    }

    def "should get response 404 - not found when update nonexistent company from id 100"() {
        given:
        def company3 = TestHelper.company(3)
        def company3Json = jsonService.objectToJson(company3)

        expect:
        mockMvc.perform(MockMvcRequestBuilders.put("/companies/100")
                .content(company3Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "should delete exact company"() {
        given:
        def company = TestHelper.company(1)
        def companyId = companyService.save(company)

        when:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.delete("/companies/$companyId"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        companyService.getByID(companyId).isEmpty()

        cleanup:
        cleanup()
    }

    def "should get response 404 - not found when delete nonexistent company from id 100"() {
        expect:
        def resultJson = mockMvc.perform(MockMvcRequestBuilders.delete("/companies/100"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
    }

    def cleanup() {
        companyService.getAll().each(i -> companyService.delete(i.id))
    }
}

package pl.futurecollars.invoicing.db.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "memory")
public class InMemoryDatabaseConfiguration {

    @Bean
    public Database<Invoice> invoiceInMemoryDatabase() {
        log.info("Creating inMemory database for invoices");
        return new InMemoryDatabase<>();
    }

    @Bean
    public Database<Company> companyInMemoryDatabase() {
        log.info("Creating inMemory database for companies");
        return new InMemoryDatabase<>();
    }
}

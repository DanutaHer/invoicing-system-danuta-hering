package pl.futurecollars.invoicing.db.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "jpa")
public class JpaDatabaseConfiguration {

    @Bean
    public Database<Invoice> invoiceJpaDatabase(InvoiceRepository invoiceRepository) {
        log.info("Creating jpa database for invoices");
        return new JpaDatabase<>(invoiceRepository);
    }

    @Bean
    public Database<Company> companyJpaDatabase(CompanyRepository invoiceRepository) {
        log.info("Creating jpa database for companies");
        return new JpaDatabase<>(invoiceRepository);
    }
}

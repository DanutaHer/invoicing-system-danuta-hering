package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@Service
@AllArgsConstructor
public class InvoiceService {

    private final Database database;

    public long save(Invoice invoice) {
        return database.save(invoice);
    }

    public Optional<Invoice> getByID(long id) {
        return database.getByID(id);
    }

    public List<Invoice> getAll() {
        return database.getAll();
    }

    public Optional<Invoice> update(long id, Invoice updatedInvoice) {
        return database.update(id, updatedInvoice);
    }

    public Optional<Invoice> delete(long id) {
        return database.delete(id);
    }
}

package pl.futurecollars.invoicing.controllers.invoice;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@AllArgsConstructor
@RestController
@Slf4j
public class InvoiceController implements InvoiceApi {

    private final InvoiceService invoiceService;

    @Override
    public ResponseEntity<List<Invoice>> getAll() {
        List<Invoice> allInvoices = invoiceService.getAll();
        log.info("Get the count of all invoices: " + allInvoices.size());
        return ResponseEntity.ok(allInvoices);
    }

    @Override
    public ResponseEntity<Integer> add(@RequestBody Invoice invoice) {
        log.info("Post new invoice: " + invoice);
        return ResponseEntity.ok(invoiceService.save(invoice));
    }

    @Override
    public ResponseEntity<Invoice> getById(@PathVariable int id) {
        Optional<Invoice> optionalInvoice = invoiceService.getByID(id);
        if (optionalInvoice.isPresent()) {
            log.info("Get invoice with id: " + id);
            return ResponseEntity.ok(optionalInvoice.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<Invoice> updateById(@PathVariable int id, @RequestBody Invoice invoice) {
        Optional<Invoice> optionalInvoice = invoiceService.update(id, invoice);
        if (optionalInvoice.isPresent()) {
            log.info("Update invoice with id: " + id);
            return ResponseEntity.ok(optionalInvoice.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<Invoice> deleteById(@PathVariable int id) {
        Optional<Invoice> optionalInvoice = invoiceService.delete(id);
        if (optionalInvoice.isPresent()) {
            log.info("Delete invoice with id: " + id);
            return ResponseEntity.ok(optionalInvoice.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

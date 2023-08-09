package pl.futurecollars.invoicing.controllers;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@AllArgsConstructor
@RestController
@Slf4j
public class InvoiceController implements InvoiceApi {

  private final InvoiceService invoiceService;

  @Override
  public ResponseEntity<Integer> add(@RequestBody Invoice invoice) {
    return ResponseEntity.ok(invoiceService.save(invoice));
  }

  @Override
  public ResponseEntity<List<Invoice>> getAll() {
    List<Invoice> allInvoices = invoiceService.getAll();
    log.info("Get the count of all invoices: " + allInvoices.size());
    return ResponseEntity.ok(allInvoices);
  }

  @Override
  public ResponseEntity<Optional<Invoice>> getExactInvoice(@PathVariable("id") int id) {
    Optional<Invoice> optionalInvoice = invoiceService.getByID(id);
    if (optionalInvoice.isPresent()) {
      log.info("Get invoice with id: " + id);
      return ResponseEntity.ok(optionalInvoice);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Override
  public ResponseEntity<Optional<Invoice>> update(@PathVariable("id") int id, @RequestBody Invoice invoice) {
    Optional<Invoice> optionalInvoice = invoiceService.update(id, invoice);
    if (optionalInvoice.isPresent()) {
      log.info("Update invoice with id: " + id);
      return ResponseEntity.ok(optionalInvoice);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Override
  public ResponseEntity<Optional<Invoice>> delete(@PathVariable("id") int id) {
    Optional<Invoice> optionalInvoice = invoiceService.delete(id);
    if (optionalInvoice.isPresent()) {
      log.info("Delete invoice with id: " + id);
      return ResponseEntity.ok(optionalInvoice);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}

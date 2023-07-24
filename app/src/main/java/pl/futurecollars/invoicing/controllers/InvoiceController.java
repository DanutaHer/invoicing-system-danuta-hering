package pl.futurecollars.invoicing.controllers;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@AllArgsConstructor
@RestController
public class InvoiceController implements InvoiceApi {

  private final InvoiceService invoiceService;

  @Override
  public ResponseEntity<Integer> add(@RequestBody Invoice invoice) {
    return ResponseEntity.ok(invoiceService.save(invoice));
  }

  @Override
  public ResponseEntity<List<Invoice>> getAll() {
    List<Invoice> allInvoices = invoiceService.getAll();
    return ResponseEntity.ok(allInvoices);
  }

  @Override
  public ResponseEntity<Optional<Invoice>> getExactInvoice(@PathVariable("id") int id) {
    Optional<Invoice> optionalInvoice = invoiceService.getByID(id);
    if (optionalInvoice.isPresent()) {
      return ResponseEntity.ok(optionalInvoice);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Override
  public ResponseEntity<Optional<Invoice>> update(@PathVariable("id") int id, @RequestBody Invoice invoice) {
    Optional<Invoice> optionalInvoice = invoiceService.update(id, invoice);
    if (optionalInvoice.isPresent()) {
      return ResponseEntity.ok(optionalInvoice);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Override
  public ResponseEntity<Optional<Invoice>> delete(@PathVariable("id") int id) {
    Optional<Invoice> optionalInvoice = invoiceService.delete(id);
    if (optionalInvoice.isPresent()) {
      return ResponseEntity.ok(optionalInvoice);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}

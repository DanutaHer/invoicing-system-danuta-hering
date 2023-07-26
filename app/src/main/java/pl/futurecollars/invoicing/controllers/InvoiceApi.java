package pl.futurecollars.invoicing.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.model.Invoice;

@RequestMapping("invoices")
@Api(tags = {"invoice-controller"})
public interface InvoiceApi {

  @PostMapping
  @ApiOperation(value = "Add new invoice")
  ResponseEntity<Integer> add(@RequestBody Invoice invoice);

  @GetMapping
  @ApiOperation(value = "Get list of all invoices")
  ResponseEntity<List<Invoice>> getAll();

  @GetMapping("/{id}")
  @ApiOperation(value = "Get invoice by id")
  ResponseEntity<Optional<Invoice>> getExactInvoice(@PathVariable("id") int id);

  @PutMapping("/{id}")
  @ApiOperation(value = "Update invoice with given id")
  ResponseEntity<Optional<Invoice>> update(@PathVariable("id") int id, @RequestBody Invoice invoice);

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete invoice with given id")
  ResponseEntity<Optional<Invoice>> delete(@PathVariable("id") int id);
}

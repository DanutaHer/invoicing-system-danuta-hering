package pl.futurecollars.invoicing.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.futurecollars.invoicing.model.Invoice;

import java.util.List;
import java.util.Optional;

@Api(tags = "Invoice controller")
@RequestMapping("invoices")
public interface InvoiceApi {

    @ApiOperation(value = "Method used to add invoice")
    @PostMapping
    ResponseEntity<Integer> add(Invoice invoice);

    @ApiOperation(value = "Method used to list all invoices")
    @GetMapping
    ResponseEntity<List<Invoice>> getAll();

    @ApiOperation(value = "Method used to get exact invoice")
    @GetMapping("/{id}")
    ResponseEntity<Optional<Invoice>> getExactInvoice(int id);

    @ApiOperation(value = "Method used to update invoice")
    @PutMapping("/{id}")
    ResponseEntity<Optional<Invoice>> update(int id, Invoice invoice);

    @ApiOperation(value = "Method used to delete invoice")
    @DeleteMapping("/{id}")
    ResponseEntity<Optional<Invoice>> delete(int id);
}

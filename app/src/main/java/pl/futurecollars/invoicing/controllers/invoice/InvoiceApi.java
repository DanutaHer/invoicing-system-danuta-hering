package pl.futurecollars.invoicing.controllers.invoice;

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

@Api(tags = "Invoice controller")
@RequestMapping("invoices")
public interface InvoiceApi {

    @ApiOperation(value = "Method used to list all invoices")
    @GetMapping
    ResponseEntity<List<Invoice>> getAll();

    @ApiOperation(value = "Method used to add invoice")
    @PostMapping
    ResponseEntity<Integer> add(@RequestBody Invoice invoice);

    @ApiOperation(value = "Method used to get invoice by Id")
    @GetMapping("/{id}")
    ResponseEntity<Invoice> getById(@PathVariable int id);

    @ApiOperation(value = "Method used to update invoice by Id ")
    @PutMapping("/{id}")
    ResponseEntity<Invoice> updateById(@PathVariable int id, @RequestBody Invoice invoice);

    @ApiOperation(value = "Method used to delete invoice by Id")
    @DeleteMapping("/{id}")
    ResponseEntity<Invoice> deleteById(@PathVariable int id);
}

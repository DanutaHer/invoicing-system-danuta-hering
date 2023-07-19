package pl.futurecollars.invoicing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.model.Invoice;

@Service
public class JsonService {

  private final ObjectMapper objectMapper = new ObjectMapper();

  {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public String objectToJson(Invoice invoice) {
    try {
      return objectMapper.writeValueAsString(invoice);
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  public Invoice jsonToObject(String json) {
    try {
      return objectMapper.readValue(json, Invoice.class);
    } catch (IOException exception) {
      throw new RuntimeException(exception);

    }
  }
}

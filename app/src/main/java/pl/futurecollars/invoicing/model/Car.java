package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Car id", example = "NCV 3456")
    private long id;
    @ApiModelProperty(value = "Registration number", example = "NCV 3456")
    private String registrationNumber;
    @ApiModelProperty(value = "Personal user", example = "True")
    private boolean personalUse;
}

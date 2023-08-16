package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {

    @ApiModelProperty(value = "Registration number", example = "NCV 3456")
    private String registrationNumber;
    @ApiModelProperty(value = "Personal user", example = "True")
    private boolean personalUser;
}

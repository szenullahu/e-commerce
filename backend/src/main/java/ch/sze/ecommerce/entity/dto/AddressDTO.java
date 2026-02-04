package ch.sze.ecommerce.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressDTO {
    @NotNull
    private String street;

    @NotNull
    private String city;

    @NotNull
    private String zip;

}

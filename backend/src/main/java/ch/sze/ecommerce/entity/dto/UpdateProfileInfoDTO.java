package ch.sze.ecommerce.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileInfoDTO {

    @NotBlank(message = "Firstname is missing")
    private String firstname;

    @NotBlank(message = "Surname is missing")
    private String surname;
}

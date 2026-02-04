package ch.sze.ecommerce.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateEmailDTO {

    @NotBlank
    @Email
    private String newEmail;
}

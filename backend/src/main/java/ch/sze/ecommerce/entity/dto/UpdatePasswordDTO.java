package ch.sze.ecommerce.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordDTO {

    @NotBlank(message = "Current Password cannot be empty")
    private String oldPassword;

    @NotBlank(message = "New Password cannot be empty")
    private String newPassword;

}

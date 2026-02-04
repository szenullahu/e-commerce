package ch.sze.ecommerce.entity.dto;

import ch.sze.ecommerce.entity.ProfilePicture;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProfileDTO {

    @NotBlank(message = "Firstname is missing")
    private String firstname;

    @NotBlank(message = "Surname is missing")
    private String surname;

    @NotNull(message = "Profile picture is missing")
    private ProfilePicture profilePicture;
}

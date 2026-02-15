package ch.sze.ecommerce.entity.dto;

import ch.sze.ecommerce.entity.ProfilePicture;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UpdateProfilePictureDTO {

    @NotNull(message = "Profile picture is missing")
    private ProfilePicture profilePicture;
}

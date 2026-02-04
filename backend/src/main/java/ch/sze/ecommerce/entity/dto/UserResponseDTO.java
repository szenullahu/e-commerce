package ch.sze.ecommerce.entity.dto;

import ch.sze.ecommerce.entity.ProfilePicture;
import ch.sze.ecommerce.entity.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private String firstname;
    private String surname;
    private ProfilePicture profilePicture;
    private Role role;
}

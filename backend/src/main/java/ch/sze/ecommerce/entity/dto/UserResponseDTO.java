package ch.sze.ecommerce.entity.dto;

import ch.sze.ecommerce.entity.ProfilePicture;
import ch.sze.ecommerce.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private String firstname;
    private String surname;
    private ProfilePicture profilePicture;
    private Role role;
}

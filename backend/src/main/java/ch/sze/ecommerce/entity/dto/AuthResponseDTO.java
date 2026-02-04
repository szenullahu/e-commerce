package ch.sze.ecommerce.entity.dto;


import lombok.Value;

@Value
public class AuthResponseDTO {
    String token;
    String username;
    String role;
}

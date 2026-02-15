package ch.sze.ecommerce.entity.dto;


public record AuthResponseDTO(String token, String username, String role) {
}
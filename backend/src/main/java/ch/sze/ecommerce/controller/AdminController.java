package ch.sze.ecommerce.controller;

import ch.sze.ecommerce.entity.dto.CreateAdminDTO;
import ch.sze.ecommerce.entity.dto.UserResponseDTO;
import ch.sze.ecommerce.service.AuthService;
import ch.sze.ecommerce.service.DTOMapper;
import ch.sze.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin
public class AdminController {

    private final AuthService authService;
    private final UserService userService;
    private final DTOMapper dtoMapper;

    public AdminController(AuthService authService, UserService userService, DTOMapper dtoMapper) {
        this.authService = authService;
        this.userService = userService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> dtos = userService.getAllUsers().stream()
                .map(dtoMapper::toUserDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(dtoMapper.toUserDTO(userService.getUserById(userId)));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody @Valid CreateAdminDTO dto) {
        authService.createAdmin(dto);
        return ResponseEntity.ok("Admin Created");
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser (@PathVariable UUID userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok("User deleted");
    }
}

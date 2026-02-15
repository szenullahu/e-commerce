package ch.sze.ecommerce.controller;

import ch.sze.ecommerce.entity.UserEntity;
import ch.sze.ecommerce.entity.dto.AuthResponseDTO;
import ch.sze.ecommerce.entity.dto.LoginDTO;
import ch.sze.ecommerce.entity.dto.RegisterDTO;
import ch.sze.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService userService) {
        this.authService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginDTO user) {
        return authService.login(user);
    }
}

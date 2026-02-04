package ch.sze.ecommerce.service;

import ch.sze.ecommerce.config.UserPrincipal;
import ch.sze.ecommerce.entity.BasketEntity;
import ch.sze.ecommerce.entity.ProfilePicture;
import ch.sze.ecommerce.entity.Role;
import ch.sze.ecommerce.entity.UserEntity;
import ch.sze.ecommerce.entity.dto.AuthResponseDTO;
import ch.sze.ecommerce.entity.dto.CreateAdminDTO;
import ch.sze.ecommerce.entity.dto.LoginDTO;
import ch.sze.ecommerce.entity.dto.RegisterDTO;
import ch.sze.ecommerce.repository.BasketRepo;
import ch.sze.ecommerce.repository.UserRepo;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService {

    private final UserRepo userRepo;

    private final PasswordEncoder encoder;

    private final AuthenticationManager manager;

    private final JWTService jwtService;
    private final BasketRepo basketRepo;

    public AuthService(UserRepo userRepo, PasswordEncoder encoder, AuthenticationManager manager, JWTService jwtService, BasketRepo basketRepo) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.manager = manager;
        this.jwtService = jwtService;
        this.basketRepo = basketRepo;
    }

    public UserEntity register(RegisterDTO dto) {
        if (userRepo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-Mail already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setFirstname(dto.getFirstname());
        user.setSurname(dto.getSurname());
        user.setProfilePicture(dto.getProfilePicture());
        user.setRole(Role.CUSTOMER);
        UserEntity savedUser = userRepo.save(user);

        BasketEntity basket = new BasketEntity();
        basket.setUser(savedUser);
        basket.setItems(new ArrayList<>());
        basket.setTotalPrice(0.0);

        basketRepo.save(basket);
        return savedUser;
    }

    public AuthResponseDTO login(@Valid LoginDTO dto) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = principal.getUser();
        String token = jwtService.generateToken(principal.getUser());

        return new AuthResponseDTO(token, user.getUsername(), user.getRole().name());
    }

    @PostConstruct
    public void createInitialAdmin() {
        if (!userRepo.existsByRole(Role.ADMIN)) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setFirstname("Admin");
            admin.setSurname("User");
            admin.setRole(Role.ADMIN);
            admin.setProfilePicture(ProfilePicture.AVATAR_1);

            userRepo.save(admin);
        }
    }

    public void createAdmin(@Valid CreateAdminDTO dto) {
        if (userRepo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-Mail already exists");
        }

        UserEntity admin = new UserEntity();
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPassword(encoder.encode(dto.getPassword()));
        admin.setFirstname(dto.getFirstname());
        admin.setSurname(dto.getSurname());
        admin.setProfilePicture(dto.getProfilePicture());
        admin.setRole(Role.ADMIN);

        userRepo.save(admin);
    }
}

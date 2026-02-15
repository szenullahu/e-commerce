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
import jakarta.transaction.Transactional;
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

    @Transactional
    public AuthResponseDTO register(RegisterDTO dto) {
        validateUserDoesNotExist(dto.getUsername(), dto.getEmail());

        UserEntity user = createUserEntity(
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getFirstname(),
                dto.getSurname(),
                dto.getProfilePicture(),
                Role.CUSTOMER
        );

        UserEntity savedUser = userRepo.save(user);

        BasketEntity basket = new BasketEntity();
        basket.setUser(savedUser);
        basket.setItems(new ArrayList<>());
        basket.setTotalPrice(0.0);

        basketRepo.save(basket);
        String token = jwtService.generateToken(savedUser);

        return new AuthResponseDTO(
                token,
                savedUser.getUsername(),
                savedUser.getRole().name()
        );
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
            admin.setProfilePicture(ProfilePicture.GOJO);

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

    private void validateUserDoesNotExist(String username, String email) {
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("E-Mail already taken");
        }
    }

    private UserEntity createUserEntity(String username, String email, String rawPassword,
                                        String firstname, String surname,
                                        ProfilePicture profilePicture, Role role) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoder.encode(rawPassword));
        user.setFirstname(firstname);
        user.setSurname(surname);
        user.setProfilePicture(profilePicture);
        user.setRole(role);
        return user;
    }
}

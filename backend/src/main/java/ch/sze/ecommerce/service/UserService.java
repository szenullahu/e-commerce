package ch.sze.ecommerce.service;

import ch.sze.ecommerce.entity.UserEntity;
import ch.sze.ecommerce.entity.dto.UpdateEmailDTO;
import ch.sze.ecommerce.entity.dto.UpdatePasswordDTO;
import ch.sze.ecommerce.entity.dto.UpdateProfileDTO;
import ch.sze.ecommerce.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder encoder;

    public UserService(UserRepo userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public UserEntity getUser(UserEntity user) {
        return user;
    }

    @Transactional
    public UserEntity updateProfileInfo(UserEntity currentUser, UpdateProfileDTO dto) {
        UserEntity user = userRepo.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstname(dto.getFirstname());
        user.setSurname(dto.getSurname());

        user.setProfilePicture(dto.getProfilePicture());

        return userRepo.save(user);
    }

    @Transactional
    public UserEntity updateEmail(UserEntity currentUser, UpdateEmailDTO dto) {
        if(userRepo.existsByEmail(dto.getNewEmail()) && currentUser.getEmail().equals(dto.getNewEmail())) {
            throw new IllegalArgumentException("E-Mail already in use");
        }

        UserEntity user = userRepo.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setEmail(dto.getNewEmail());
        return userRepo.save(user);
    }

    @Transactional
    public UserEntity updatePassword(UserEntity currentUser, UpdatePasswordDTO dto) {
        UserEntity user = userRepo.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        if (encoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("The same password cannot be used again");
        }

        user.setPassword(dto.getNewPassword());
        return userRepo.save(user);
    }

    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    public UserEntity getUserById(UUID userId) {
        return userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional
    public void deleteUser(UUID userId) {
        if(!userRepo.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepo.deleteById(userId);
    }
}

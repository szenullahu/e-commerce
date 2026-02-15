package ch.sze.ecommerce.service;

import ch.sze.ecommerce.entity.UserEntity;
import ch.sze.ecommerce.entity.dto.UpdateEmailDTO;
import ch.sze.ecommerce.entity.dto.UpdatePasswordDTO;
import ch.sze.ecommerce.entity.dto.UpdateProfileInfoDTO;
import ch.sze.ecommerce.entity.dto.UpdateProfilePictureDTO;
import ch.sze.ecommerce.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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
    public UserEntity updateProfileInfo(UserEntity currentUser, UpdateProfileInfoDTO dto) {
        UserEntity user = userRepo.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstname(dto.getFirstname());
        user.setSurname(dto.getSurname());

        return userRepo.save(user);
    }

    public UserEntity updateProfilePicture(UserEntity currentUser, @Valid UpdateProfilePictureDTO dto) {
        UserEntity user = userRepo.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setProfilePicture(dto.getProfilePicture());

        return userRepo.save(user);
    }

    @Transactional
    public UserEntity updateEmail(UserEntity currentUser, UpdateEmailDTO dto) {
        if (!currentUser.getEmail().equals(dto.getNewEmail())) {
            if (userRepo.existsByEmail(dto.getNewEmail())) {
                throw new IllegalArgumentException("E-Mail already in use");
            }
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

        user.setPassword(encoder.encode(dto.getNewPassword()));
        return userRepo.save(user);
    }

    @Transactional
    public void deleteUser(UserEntity currentUser) {
        if(!userRepo.existsById(currentUser.getId())) {
            throw new EntityNotFoundException("User not found");
        }
        userRepo.deleteById(currentUser.getId());
    }

    @Transactional
    public void deleteUserById(UUID userId) {
        if(!userRepo.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepo.deleteById(userId);
    }

    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    public UserEntity getUserById(UUID userId) {
        return userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }


}

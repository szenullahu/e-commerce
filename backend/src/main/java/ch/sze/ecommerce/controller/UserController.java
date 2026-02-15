package ch.sze.ecommerce.controller;

import ch.sze.ecommerce.config.UserPrincipal;
import ch.sze.ecommerce.entity.dto.*;
import ch.sze.ecommerce.service.DTOMapper;
import ch.sze.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
public class UserController {

    private final UserService userService;
    private final DTOMapper dtoMapper;

    public UserController(UserService userService, DTOMapper dtoMapper) {
        this.userService = userService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> getUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(dtoMapper.toUserDTO(userService.getUser(userPrincipal.getUser())));
    }

    @PutMapping("profile")
    public ResponseEntity<UserResponseDTO> updateProfileInfo(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid UpdateProfileInfoDTO dto) {
        return ResponseEntity.ok(dtoMapper.toUserDTO(userService.updateProfileInfo(userPrincipal.getUser(), dto)));
    }

    @PatchMapping("/profile-picture")
    public ResponseEntity<UserResponseDTO> updateProfilePicture(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid UpdateProfilePictureDTO dto) {
        return ResponseEntity.ok(dtoMapper.toUserDTO(userService.updateProfilePicture(userPrincipal.getUser(), dto)));
    }

    @PutMapping("/email")
    public ResponseEntity<UserResponseDTO> updateEmail(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid UpdateEmailDTO dto) {
        return ResponseEntity.ok(dtoMapper.toUserDTO(userService.updateEmail(userPrincipal.getUser(), dto)));
    }

    @PutMapping("/password")
    public ResponseEntity<UserResponseDTO> updatePassword(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid UpdatePasswordDTO dto) {
        return ResponseEntity.ok(dtoMapper.toUserDTO(userService.updatePassword(userPrincipal.getUser(), dto)));
    }

}

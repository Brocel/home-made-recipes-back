package com.example.hmrback.api.controller.user;

import com.example.hmrback.mapper.dto.UserDTOMapper;
import com.example.hmrback.model.User;
import com.example.hmrback.model.request.UpdateUserRequest;
import com.example.hmrback.model.response.UserResponse;
import com.example.hmrback.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.hmrback.constant.ControllerConstants.BASE_PATH;
import static com.example.hmrback.constant.ControllerConstants.USER;

@RestController
@RequestMapping(BASE_PATH + USER)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @recipeSecurity.isAuthor(#id))")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable
            String id,
            @Valid
            @RequestBody
            UpdateUserRequest request
    ) {
        User userInput = UserDTOMapper.toUser(request);
        User updatedUser = this.userService.updateUser(id, userInput);
        return ResponseEntity.ok(userToResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @recipeSecurity.isAuthor(#id))")
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            String id
    ) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Conversion helper
    private UserResponse userToResponse(User user) {
        return new UserResponse(
                user.id(),
                user.firstName(),
                user.lastName(),
                user.username(),
                user.email(),
                user.birthDate(),
                user.inscriptionDate(),
                user.avatar(),
                user.roles()
        );
    }
}

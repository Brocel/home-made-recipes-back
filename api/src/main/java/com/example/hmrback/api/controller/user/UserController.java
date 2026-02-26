package com.example.hmrback.api.controller.user;

import com.example.hmrback.model.User;
import com.example.hmrback.model.request.UserUpdateRequest;
import com.example.hmrback.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.hmrback.constant.ControllerConstants.BASE_PATH;
import static com.example.hmrback.constant.ControllerConstants.USER;

@RestController
@RequestMapping(BASE_PATH + USER)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @recipeSecurity.isAuthor(#id))")
    public ResponseEntity<User> updateUser(
            @PathVariable
            String id,
            @Valid
            @RequestBody
            UserUpdateRequest request
    ) {
        return ResponseEntity.ok(this.userService.updateUser(id,
                                                             request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @recipeSecurity.isAuthor(#id))")
    public ResponseEntity<User> deleteUser(
            @PathVariable
            String id
    ) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent()
                             .build();
    }
}

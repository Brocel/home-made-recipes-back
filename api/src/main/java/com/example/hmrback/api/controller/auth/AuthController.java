package com.example.hmrback.api.controller.auth;

import com.example.hmrback.model.request.LoginRequest;
import com.example.hmrback.model.request.RegisterRequest;
import com.example.hmrback.model.response.AuthResponse;
import com.example.hmrback.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.hmrback.constant.ControllerConstants.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH + "/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> existsByUsername(@RequestParam("username") String username) {
        boolean existByUsername = this.authenticationService.existsByUsername(username);

        return ResponseEntity.ok(existByUsername);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateWithGoogle(
            @RequestBody
            @Valid
            LoginRequest request) {

        AuthResponse response = this.authenticationService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody
            @Valid
            RegisterRequest request) {

        AuthResponse response = this.authenticationService.register(request);

        return ResponseEntity.ok(response);
    }
}

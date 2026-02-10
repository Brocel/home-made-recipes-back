package com.example.hmrback.api.controller.auth;

import com.example.hmrback.model.request.GoogleAuthRequest;
import com.example.hmrback.model.request.RegistrationRequest;
import com.example.hmrback.model.response.AuthResponse;
import com.example.hmrback.service.auth.GoogleAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.example.hmrback.constant.ControllerConstants.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class AuthController {

    private final GoogleAuthService googleAuthService;

    @Autowired
    public AuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/auth/google")
    public ResponseEntity<AuthResponse> authenticateWithGoogle(
            @RequestBody
            @Valid
            GoogleAuthRequest request) throws GeneralSecurityException, IOException {

        AuthResponse response = this.googleAuthService.authenticateViaGoogle(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody
            @Valid
            RegistrationRequest request) {

        AuthResponse response = this.googleAuthService.register(request);

        return ResponseEntity.ok(response);
    }
}

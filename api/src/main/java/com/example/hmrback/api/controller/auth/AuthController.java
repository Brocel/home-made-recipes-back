package com.example.hmrback.api.controller.auth;

import com.example.hmrback.model.request.LoginRequest;
import com.example.hmrback.model.request.RegisterRequest;
import com.example.hmrback.model.response.AuthResponse;
import com.example.hmrback.service.auth.AuthenticationService;
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

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/auth/google")
    public ResponseEntity<AuthResponse> authenticateWithGoogle(
            @RequestBody
            @Valid
            LoginRequest request) throws GeneralSecurityException, IOException {

        AuthResponse response = this.authenticationService.authenticateViaGoogle(request);

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

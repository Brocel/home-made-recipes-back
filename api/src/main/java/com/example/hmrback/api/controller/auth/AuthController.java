package com.example.hmrback.api.controller.auth;

import com.example.hmrback.model.request.GoogleAuthRequest;
import com.example.hmrback.model.response.AuthResponse;
import com.example.hmrback.model.response.AuthResult;
import com.example.hmrback.service.auth.GoogleAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class AuthController {

    private final GoogleAuthService googleAuthService;

    public AuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/auth/google")
    public ResponseEntity<AuthResponse> authenticateWithGoogle(
        @RequestBody
        @Valid
        GoogleAuthRequest request,
        HttpServletRequest httpRequest) {

        AuthResult result = googleAuthService.processGoogleLogin(request.idToken());
        Collection<? extends GrantedAuthority> authorities = result.authorities();

        Object principal = result.principal();

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        httpRequest.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        AuthResponse response = new AuthResponse(result.username(), result.displayName());
        return ResponseEntity.ok(response);
    }
}

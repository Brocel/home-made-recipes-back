package com.example.hmrback.service.auth;

import com.example.hmrback.exception.AuthException;
import com.example.hmrback.mapper.UserMapper;
import com.example.hmrback.model.request.LoginRequest;
import com.example.hmrback.model.request.RegisterRequest;
import com.example.hmrback.model.response.AuthResponse;
import com.example.hmrback.persistence.entity.RoleEntity;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.enums.RoleEnum;
import com.example.hmrback.persistence.repository.RoleRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.utils.DateUtils;
import com.example.hmrback.utils.JwtUtils;
import com.example.hmrback.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Value("${admin.emails}")
    String adminEmailsRaw;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expiration-minutes}")
    private Long expirationMinutes;

    /**
     * TODO: régler ça à la volée lors de la saisie dans le front
     *
     * @param username
     * @return
     */
    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    /**
     * TODO
     *
     * @param req
     * @return
     */
    public AuthResponse register(RegisterRequest req) {

        if (this.userRepository.existsByEmail(req.email()))
            throw new RuntimeException("Email already used"); // TODO: create custom exception

        // User creation
        UserEntity user = new UserEntity();
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setBirthDate(DateUtils.parseLocalDate(req.birthDate()));
        user.setInscriptionDate(LocalDate.now());
        user.setPassword(passwordEncoder.encode(req.password()));

        // Roles
        Set<RoleEntity> roles = new HashSet<>();
        Optional<RoleEntity> roleUser = this.roleRepository.findByName(RoleEnum.ROLE_USER);
        roleUser.ifPresent(roles::add);

        Optional<RoleEntity> roleAdmin = this.roleRepository.findByName(RoleEnum.ROLE_ADMIN);
        Set<String> adminEmails = UserUtils.getCommaSeparatedEmails(adminEmailsRaw);
        if (adminEmails.contains(user.getEmail())) {
            roleAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);

        UserEntity savedUser = userRepository.save(user);

        String token = JwtUtils.generateToken(savedUser,
                                              expirationMinutes,
                                              secretKey);

        return new AuthResponse(
                token,
                this.userMapper.toModel(savedUser)
        );
    }

    /**
     * TODO
     *
     * @param req
     * @return
     */
    public AuthResponse login(LoginRequest req) {

        UserEntity user = userRepository.findByEmail(req.email())
                                        .orElseThrow(() -> new AuthException(HttpStatus.UNAUTHORIZED,
                                                                             LogLevel.WARN,
                                                                             "Invalid email"));

        if (!passwordEncoder.matches(req.password(),
                                     user.getPassword())) {
            throw new AuthException(HttpStatus.UNAUTHORIZED,
                                    LogLevel.WARN,
                                    "Invalid password");
        }

        String token = JwtUtils.generateToken(user,
                                              expirationMinutes,
                                              secretKey);

        return new AuthResponse(
                token,
                this.userMapper.toModel(user)
        );
    }
}



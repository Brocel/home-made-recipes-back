package com.example.hmrback.service.auth;

import com.example.hmrback.exception.AuthException;
import com.example.hmrback.exception.util.ExceptionMessageEnum;
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
     * Check if the User with given username exists in DB
     *
     * @param username String
     * @return boolean
     */
    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    /**
     * Registers a new user in DB
     *
     * <ul>
     *     <li>Checks if a User with given email already exists in DB</li>
     *     <li>Build UserEntity</li>
     *     <li>Save UserEntity in DB</li>
     *     <li>Generates a token</li>
     * </ul>
     *
     * @param req The params form register request
     * @return AuthResponse with token and created User
     * @throws AuthException if email already exists in DB
     */
    public AuthResponse register(RegisterRequest req) throws AuthException {

        if (this.userRepository.existsByEmail(req.email())) {
            throw new AuthException(HttpStatus.UNPROCESSABLE_ENTITY,
                                    LogLevel.WARN,
                                    ExceptionMessageEnum.EMAIL_ALREADY_EXISTS);
        }

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

        Set<String> adminEmails = UserUtils.getCommaSeparatedEmails(adminEmailsRaw);
        if (adminEmails.contains(user.getEmail())) {
            Optional<RoleEntity> roleAdmin = this.roleRepository.findByName(RoleEnum.ROLE_ADMIN);
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
     * Log a user in.
     *
     * <ul>
     *     <li>Find the user, using email</li>
     *     <li>Check the password</li>
     *     <li>Generates a token</li>
     * </ul>
     *
     * @param req The params from login request
     * @return AuthResponse with token and User infos
     * @throws AuthException when email is not found OR password doesn't match
     */
    public AuthResponse login(LoginRequest req) throws AuthException {

        UserEntity user = userRepository.findByEmail(req.email())
                                        .orElseThrow(() -> new AuthException(HttpStatus.UNAUTHORIZED,
                                                                             LogLevel.WARN,
                                                                             ExceptionMessageEnum.EMAIL_NOT_FOUND));

        if (!passwordEncoder.matches(req.password(),
                                     user.getPassword())) {
            throw new AuthException(HttpStatus.UNAUTHORIZED,
                                    LogLevel.WARN,
                                    ExceptionMessageEnum.INVALID_PASSWORD);
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



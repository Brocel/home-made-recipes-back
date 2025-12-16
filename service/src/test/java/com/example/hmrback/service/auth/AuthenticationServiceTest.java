package com.example.hmrback.service.auth;

import com.example.hmrback.BaseTU;
import com.example.hmrback.exception.AuthException;
import com.example.hmrback.mapper.UserMapper;
import com.example.hmrback.model.User;
import com.example.hmrback.model.request.RegisterRequest;
import com.example.hmrback.model.response.AuthResponse;
import com.example.hmrback.persistence.entity.RoleEntity;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.enums.RoleEnum;
import com.example.hmrback.persistence.repository.RoleRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.utils.test.CommonTestUtils;
import com.example.hmrback.utils.test.EntityTestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static com.example.hmrback.exception.util.ExceptionMessageConstants.ROLE_NOT_FOUND_MESSAGE;
import static com.example.hmrback.exception.util.ExceptionMessageConstants.USER_EMAIL_ALREADY_EXISTS_MESSAGE;
import static com.example.hmrback.utils.test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest extends BaseTU {

    @InjectMocks
    private AuthenticationService service;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    private static RoleEntity userRole;
    private static UserEntity user;

    private static RegisterRequest registerRequest;

    private static final LocalDate now = LocalDate.now();

    @BeforeAll
    static void setup() {
        // Role
        userRole = EntityTestUtils.buildRoleEntity();

        // User
        user = EntityTestUtils.buildUserEntity(NUMBER_1, false);

        // Requests
        registerRequest = CommonTestUtils.buildRegisterRequest(NUMBER_1);

    }

    @Test
    @Order(1)
    void register() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(roleRepository.findByName(any(RoleEnum.class))).thenReturn(Optional.ofNullable(userRole));
        when(userMapper.toEntity(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        AuthResponse result = service.register(registerRequest);

        assertNotNull(result, NOT_NULL_MESSAGE.formatted("AuthResponse"));
        assertNotNull(result.username(), NOT_NULL_MESSAGE.formatted("token"));

        assertEquals(now, user.getInscriptionDate(), SHOULD_BE_EQUALS_MESSAGE.formatted("Inscription date", now));

        verify(userRepository, times(1)).existsByEmail(EMAIL.formatted(NUMBER_1));
        verify(userRepository, times(1)).existsByUsername(USERNAME.formatted(NUMBER_1));
        verify(roleRepository, times(1)).findByName(RoleEnum.ROLE_USER);
        verify(userMapper, times(1)).toEntity(any(User.class));
        verify(passwordEncoder, times(1)).encode(PASSWORD);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @Order(2)
    void register_whenUserAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        AuthException ex = assertThrows(AuthException.class, () -> service.register(registerRequest));

        assertNotNull(ex, NOT_NULL_MESSAGE.formatted("Exception"));
        assertEquals(USER_EMAIL_ALREADY_EXISTS_MESSAGE.formatted(EMAIL.formatted(NUMBER_1)),
            ex.getMessage(),
            EXCEPTION_MESSAGE_SHOULD_MATCH);

        verify(userRepository, times(1)).existsByEmail(EMAIL.formatted(NUMBER_1));
        verify(roleRepository, times(0)).findByName(RoleEnum.ROLE_USER);
        verify(userMapper, times(0)).toEntity(any(User.class));
        verify(passwordEncoder, times(0)).encode(PASSWORD);
        verify(userRepository, times(0)).save(any(UserEntity.class));
    }

    @Test
    @Order(3)
    void register_whenRoleNotFound() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(roleRepository.findByName(any(RoleEnum.class))).thenReturn(Optional.empty());

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.register(registerRequest));

        assertNotNull(ex, NOT_NULL_MESSAGE.formatted("Exception"));
        assertEquals(ROLE_NOT_FOUND_MESSAGE.formatted("ROLE_USER"), ex.getMessage(), EXCEPTION_MESSAGE_SHOULD_MATCH);

        verify(userRepository, times(1)).existsByEmail(EMAIL.formatted(NUMBER_1));
        verify(userRepository, times(1)).existsByUsername(USERNAME.formatted(NUMBER_1));
        verify(roleRepository, times(1)).findByName(RoleEnum.ROLE_USER);
        verify(userMapper, times(0)).toEntity(any(User.class));
        verify(passwordEncoder, times(0)).encode(PASSWORD);
        verify(userRepository, times(0)).save(any(UserEntity.class));
    }

}

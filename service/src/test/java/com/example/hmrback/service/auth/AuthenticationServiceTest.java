package com.example.hmrback.service.auth;

import com.example.hmrback.BaseTU;
import com.example.hmrback.exception.AuthException;
import com.example.hmrback.exception.util.ExceptionMessageEnum;
import com.example.hmrback.mapper.UserMapper;
import com.example.hmrback.model.User;
import com.example.hmrback.model.request.LoginRequest;
import com.example.hmrback.model.request.RegisterRequest;
import com.example.hmrback.model.response.AuthResponse;
import com.example.hmrback.persistence.entity.RoleEntity;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.enums.RoleEnum;
import com.example.hmrback.persistence.repository.RoleRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.utils.test.CommonTestUtils;
import com.example.hmrback.utils.test.EntityTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.example.hmrback.utils.test.CommonTestUtils.ADMIN_EMAIL;
import static com.example.hmrback.utils.test.TestConstants.NUMBER_1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest extends BaseTU {

    private static final String USERNAME = "Username";

    @InjectMocks
    private AuthenticationService service;

    // Repo
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    // Other
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    // Setup
    private static UserEntity userEntity;
    private static RoleEntity userRole;
    private static RoleEntity adminRole;
    private static User userModel;
    private static LoginRequest loginRequest;
    private static RegisterRequest registerRequest;

    @BeforeEach
    void valueSetup() {
        ReflectionTestUtils.setField(service,
                                     "adminEmailsRaw",
                                     ADMIN_EMAIL);
        ReflectionTestUtils.setField(service,
                                     "secretKey",
                                     "fake-secret-key-just-for-unit-tests-666_@!#");
        ReflectionTestUtils.setField(service,
                                     "expirationMinutes",
                                     999L);
    }

    @BeforeAll
    static void setup() {
        // Entities
        userEntity = EntityTestUtils.buildUserEntity(NUMBER_1,
                                                     false);

        adminRole = EntityTestUtils.buildRoleEntity(true);
        userRole = EntityTestUtils.buildRoleEntity(false);

        // Models
        userModel = ModelTestUtils.buildUser(1L,
                                             false);

        // Requests
        registerRequest = CommonTestUtils.buildRegisterRequest(userModel,
                                                               false);
        loginRequest = CommonTestUtils.buildLoginRequest();
    }

    // Tests
    @Test
    @Order(1)
    void existsByUsername_whenUserExists_thenReturnTrue() {
        // Mocks
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // Test
        boolean result = service.existsByUsername(USERNAME);

        // Assertions
        assertTrue(result);

        verify(userRepository,
               times(1)).existsByUsername(USERNAME);
    }

    @Test
    @Order(2)
    void existsByUsername_whenUserDoesNotExists_thenReturnFalse() {
        // Mocks
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        // Test
        boolean result = service.existsByUsername(USERNAME);

        // Assertions
        assertFalse(result);

        verify(userRepository,
               times(1)).existsByUsername(USERNAME);
    }

    @Test
    @Order(3)
    void register_withClassicUser() throws AuthException {
        // Mocks
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.ofNullable(userRole));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toModel(any(UserEntity.class))).thenReturn(userModel);

        // Test
        AuthResponse result = service.register(registerRequest);

        // Assertions
        assertNotNull(result);
        assertNull(result.token());
        assertNotNull(result.user());

        verify(userRepository,
               times(1)).existsByEmail(anyString());
        verify(roleRepository,
               times(1)).findByName(RoleEnum.ROLE_USER);
        verify(roleRepository,
               times(0)).findByName(RoleEnum.ROLE_ADMIN);
        verify(userRepository,
               times(1)).save(any(UserEntity.class));
        verify(userMapper,
               times(1)).toModel(any(UserEntity.class));
    }

    @Test
    @Order(4)
    void register_withAdminUser() throws AuthException {
        // Custom Setup
        RegisterRequest adminRegisterRequest = CommonTestUtils.buildRegisterRequest(userModel,
                                                                                    true);

        // Mocks
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.ofNullable(userRole));
        when(roleRepository.findByName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.ofNullable(adminRole));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toModel(any(UserEntity.class))).thenReturn(userModel);

        // Test
        AuthResponse result = service.register(adminRegisterRequest);

        // Assertions
        assertNotNull(result);
        assertNull(result.token());
        assertNotNull(result.user());

        verify(userRepository,
               times(1)).existsByEmail(anyString());
        verify(roleRepository,
               times(1)).findByName(RoleEnum.ROLE_USER);
        verify(roleRepository,
               times(1)).findByName(RoleEnum.ROLE_ADMIN);
        verify(userRepository,
               times(1)).save(any(UserEntity.class));
        verify(userMapper,
               times(1)).toModel(any(UserEntity.class));
    }

    @Test
    @Order(5)
    void register_withExistingUser_thenThrowsException() {
        // Mocks
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Test
        AuthException ex = assertThrows(AuthException.class,
                                        () -> service.register(registerRequest));

        // Assertions
        assertNotNull(ex);
        assertNotNull(ex.getExceptionEnum());
        assertNotNull(ex.getStatus());

        assertEquals(ExceptionMessageEnum.EMAIL_ALREADY_EXISTS,
                     ex.getExceptionEnum());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,
                     ex.getStatus());

        verify(userRepository,
               times(1)).existsByEmail(anyString());
        verify(roleRepository,
               times(0)).findByName(RoleEnum.ROLE_USER);
        verify(roleRepository,
               times(0)).findByName(RoleEnum.ROLE_ADMIN);
        verify(userRepository,
               times(0)).save(any(UserEntity.class));
        verify(userMapper,
               times(0)).toModel(any(UserEntity.class));
    }

    @Test
    @Order(6)
    void login() throws AuthException {
        // Mocks
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userMapper.toModel(any(UserEntity.class))).thenReturn(userModel);

        // Test
        AuthResponse result = service.login(loginRequest);

        // Assertions
        assertNotNull(result);
        assertNotNull(result.token());
        assertNotNull(result.user());

        verify(userRepository,
               times(1)).findByEmail(anyString());
        verify(passwordEncoder,
               times(1)).matches(anyString(), anyString());
        verify(userMapper,
               times(1)).toModel(any(UserEntity.class));
    }

    @Test
    @Order(7)
    void login_whenWrongPassord_thenThrowsException() {
        // Mocks
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Test
        AuthException ex = assertThrows(AuthException.class,
                                        () -> service.login(loginRequest));

        // Assertions
        assertNotNull(ex);
        assertNotNull(ex.getExceptionEnum());
        assertNotNull(ex.getStatus());

        assertEquals(ExceptionMessageEnum.INVALID_PASSWORD,
                     ex.getExceptionEnum());
        assertEquals(HttpStatus.UNAUTHORIZED,
                     ex.getStatus());

        verify(userRepository,
               times(1)).findByEmail(anyString());
        verify(passwordEncoder,
               times(1)).matches(anyString(), anyString());
        verify(userMapper,
               times(0)).toModel(any(UserEntity.class));
    }

    @Test
    @Order(8)
    void login_whenUserAlreadyExists_thenThrowsException() {
        // Mocks
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Test
        AuthException ex = assertThrows(AuthException.class,
                                        () -> service.login(loginRequest));

        // Assertions
        assertNotNull(ex);
        assertNotNull(ex.getExceptionEnum());
        assertNotNull(ex.getStatus());

        assertEquals(ExceptionMessageEnum.EMAIL_NOT_FOUND,
                     ex.getExceptionEnum());
        assertEquals(HttpStatus.UNAUTHORIZED,
                     ex.getStatus());

        verify(userRepository,
               times(1)).findByEmail(anyString());
        verify(passwordEncoder,
               times(0)).matches(anyString(), anyString());
        verify(userMapper,
               times(0)).toModel(any(UserEntity.class));
    }
}

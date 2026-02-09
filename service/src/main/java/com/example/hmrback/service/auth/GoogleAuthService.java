package com.example.hmrback.service.auth;

import com.example.hmrback.mapper.UserMapper;
import com.example.hmrback.model.Role;
import com.example.hmrback.model.User;
import com.example.hmrback.model.UserBasicInfo;
import com.example.hmrback.model.request.GoogleAuthRequest;
import com.example.hmrback.model.request.RegistrationRequest;
import com.example.hmrback.model.response.AuthResponse;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.enums.RoleEnum;
import com.example.hmrback.service.user.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleAuthService.class);

    private final JwtDecoder googleJwtDecoder;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    private final UserService userService;

    private final UserMapper userMapper;

    /**
     * TODO
     *
     * @param request
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public AuthResponse authenticateViaGoogle(@Valid GoogleAuthRequest request)
            throws GeneralSecurityException, IOException {

        String idTokenString = request.idToken();
        GoogleIdToken idToken;

        if (StringUtils.isNotBlank(idTokenString)) {
            idToken = googleIdTokenVerifier.verify(idTokenString);
        } else {
            // TODO: throw custom exception
            throw new GeneralSecurityException("Google Id Token shouldn't be null");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");
        String username = (String) payload.get("name");

        Optional<UserEntity> optionalUserEntity = this.userService.findByEmail(email);

        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            return new AuthResponse(
                    jwtService.generate(userEntity),
                    userMapper.toModel(userEntity),
                    false,
                    null
            );
        }

        return new AuthResponse(
                null,
                null,
                true,
                new UserBasicInfo(email,
                                  firstName,
                                  lastName,
                                  username)
        );
    }

    public AuthResponse register(@Valid RegistrationRequest request) {
        User user = new User(
                null,
                request.firstName(),
                request.lastName(),
                request.username(),
                request.email(),
                request.birthDate(),
                LocalDate.now()
                         .toString(),
                Set.of(new Role(2L,
                                RoleEnum.ROLE_USER.name()))
        );

        UserEntity savedUser = this.userService.createUser(user);

        String jwt = this.jwtService.generate(savedUser);

        return new AuthResponse(
                jwt,
                userMapper.toModel(savedUser),
                false,
                null
        );
    }
}


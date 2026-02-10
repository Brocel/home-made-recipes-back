package com.example.hmrback.service.user;

import com.example.hmrback.mapper.UserMapper;
import com.example.hmrback.model.User;
import com.example.hmrback.persistence.entity.OAuth2AccountEntity;
import com.example.hmrback.persistence.entity.RoleEntity;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.enums.RoleEnum;
import com.example.hmrback.persistence.repository.OAuth2AccountRepository;
import com.example.hmrback.persistence.repository.RoleRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.service.auth.AuthenticationService;
import com.example.hmrback.utils.NormalizeUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OAuth2AccountRepository oauth2AccountRepository;

    private final UserCreationService userCreationService;

    private final UserMapper userMapper;

    @Value("${admin.emails}")
    String adminEmailsRaw;

    public Optional<UserEntity> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public UserEntity createUser(User user) {
        LOG.info("Saving new user :: {}", user.email());
        return this.userRepository.save(this.userMapper.toEntity(user));
    }

    // TODO: refacto pour coller à la nouvelle logique

//    @Transactional
//    public UserEntity findOrCreateAndLinkUser(String provider, String providerId, String email, String name) {
//        Optional<OAuth2AccountEntity> existingOauth = oauth2AccountRepository.findByProviderAndProviderId(provider, providerId);
//        if (existingOauth.isPresent()) {
//            return existingOauth.get().getUser();
//        }
//
//        Optional<UserEntity> userByEmail = userRepository.findByEmail(email);
//        UserEntity user;
//        if (userByEmail.isPresent()) {
//            user = userByEmail.get();
//            userCreationService.createAndSaveOauth2Account(provider, providerId, user);
//            LOG.info("Linked Google account to existing user id={} email={}", user.getId(), email);
//        } else {
//            user = userCreationService.createUserFromOidc(email, name);
//            userCreationService.createAndSaveOauth2Account(provider, providerId, user);
//            LOG.info("Created new user id={} email={}", user.getId(), email);
//        }
//
//        // Grant ROLE_ADMIN if email in admin list
//        Set<String> adminEmails = NormalizeUtils.normalizeListInString(adminEmailsRaw);
//        if (adminEmails.contains(email.toLowerCase(Locale.ROOT))) {
//            RoleEntity adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
//                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found"));
//            boolean hasAdmin = user.getRoles().stream().anyMatch(r -> r.getName() == RoleEnum.ROLE_ADMIN);
//            if (!hasAdmin) {
//                user.getRoles().add(adminRole);
//                userRepository.save(user);
//                LOG.info("Granted ROLE_ADMIN to user id={} email={}", user.getId(), email);
//            }
//        }
//
//        return user;
//    }
}

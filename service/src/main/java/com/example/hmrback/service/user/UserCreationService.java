package com.example.hmrback.service.user;

import com.example.hmrback.persistence.entity.OAuth2AccountEntity;
import com.example.hmrback.persistence.entity.RoleEntity;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.enums.RoleEnum;
import com.example.hmrback.persistence.repository.OAuth2AccountRepository;
import com.example.hmrback.persistence.repository.RoleRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserCreationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OAuth2AccountRepository oauth2AccountRepository;

    private final UsernameService usernameService;

    @Transactional
    public void createAndSaveOauth2Account(String provider, String providerId, UserEntity user) {
        OAuth2AccountEntity account = new OAuth2AccountEntity();
        account.setProvider(provider);
        account.setProviderId(providerId);
        account.setUser(user);
        oauth2AccountRepository.save(account);
    }

    @Transactional
    public UserEntity createUserFromOidc(String email, String name) {
        RoleEntity userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
            .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setUsername(usernameService.generateUniqueUsernameFromEmail(email));
        user.setDisplayName(name != null ? name : user.getUsername());
        user.setInscriptionDate(LocalDate.now());
        user.getRoles().add(userRole);
        userRepository.save(user);
        return user;
    }
}

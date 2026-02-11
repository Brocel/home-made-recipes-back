package com.example.hmrback.service.user;

import com.example.hmrback.mapper.UserMapper;
import com.example.hmrback.model.User;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.repository.RoleRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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

}

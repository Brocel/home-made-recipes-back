package com.example.hmrback.service.user;

import com.example.hmrback.exception.CustomEntityNotFoundException;
import com.example.hmrback.exception.util.ExceptionMessageEnum;
import com.example.hmrback.mapper.UserMapper;
import com.example.hmrback.model.User;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public Optional<UserEntity> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public UserEntity createUser(User user) {
        LOG.info("Saving new user :: {}",
                user.email());
        return this.userRepository.save(this.userMapper.toEntity(user));
    }

    public User updateUser(String userId,
                           @Valid User user) throws CustomEntityNotFoundException {

        LOG.info("Updating user: {}",
                userId);

        UserEntity existingUser = this.userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new CustomEntityNotFoundException(ExceptionMessageEnum.USER_NOT_FOUND, userId));

        userMapper.updateEntityFromModel(user, existingUser);

        return userMapper.toModel(this.userRepository.saveAndFlush(existingUser));
    }

    public void deleteUser(String userId) {

        LOG.info("Deleting user: {}",
                userId);

        Optional<UserEntity> existingUser = this.userRepository.findById(UUID.fromString(userId));

        existingUser.ifPresentOrElse(userRepository::delete,
                () -> {
                    throw new CustomEntityNotFoundException(ExceptionMessageEnum.USER_NOT_FOUND, userId);
                });
    }
}

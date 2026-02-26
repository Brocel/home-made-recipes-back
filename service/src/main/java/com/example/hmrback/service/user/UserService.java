package com.example.hmrback.service.user;

import com.example.hmrback.exception.CustomEntityNotFoundException;
import com.example.hmrback.exception.util.ExceptionMessageEnum;
import com.example.hmrback.mapper.UserMapper;
import com.example.hmrback.model.User;
import com.example.hmrback.model.request.UserUpdateRequest;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.utils.UserUtils;
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
                           @Valid UserUpdateRequest request) {

        LOG.info("Updating user: {}",
                 userId);

        Optional<UserEntity> existingUser = this.userRepository.findById(UUID.fromString(userId));

        if (existingUser.isPresent()) {
            UserEntity user = UserUtils.updateUser(existingUser.get(),
                                                   request);
            return this.userMapper.toModel(this.userRepository.save(user));
        } else {
            throw new CustomEntityNotFoundException(ExceptionMessageEnum.USER_NOT_FOUND,
                                                    ExceptionMessageEnum.USER_NOT_FOUND.getMessage()
                                                                                       .formatted(userId));
        }
    }

    public void deleteUser(String userId) {

        LOG.info("Deleting user: {}",
                 userId);

        Optional<UserEntity> existingUser = this.userRepository.findById(UUID.fromString(userId));

        existingUser.ifPresentOrElse(userRepository::delete,
                                     () -> {
                                         throw new CustomEntityNotFoundException(ExceptionMessageEnum.USER_NOT_FOUND,
                                                                                 ExceptionMessageEnum.USER_NOT_FOUND.getMessage()
                                                                                                                    .formatted(userId));
                                     });
    }
}

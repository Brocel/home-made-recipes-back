package com.example.hmrback.service.user;

import com.example.hmrback.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsernameService {

    private final UserRepository userRepository;

    public String generateUniqueUsernameFromEmail(String email) {
        String base = email.split("@")[0].replaceAll("[^A-Za-z0-9._-]", "");
        String candidate = base;
        int suffix = 0;
        while (userRepository.existsByUsername(candidate)) {
            suffix++;
            candidate = base + suffix;
        }
        return candidate;
    }

}

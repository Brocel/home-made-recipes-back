package com.example.hmrback.auth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    private static final Logger log = LoggerFactory.getLogger(PasswordHasher.class);

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String rawPassword = "pouet-pouet";
//        String encodedPassword = encoder.encode(rawPassword);
//        log.info(encodedPassword);
    }
}

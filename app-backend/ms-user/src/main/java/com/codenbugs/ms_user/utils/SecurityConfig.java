package com.codenbugs.ms_user.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Value("${security.password-secret}")
    private String passwordSecret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder(
                passwordSecret,
                1,
                32,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512
        );
    }
}

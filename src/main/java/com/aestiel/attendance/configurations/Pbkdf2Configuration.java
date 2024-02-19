package com.aestiel.attendance.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import static org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256;

@Configuration
public class Pbkdf2Configuration {
    private static final int SALT_LENGTH = 8;
    private static final int ITERATIONS = 64000;

    @Value("${attendance.pbkdf2.secret}")
    private String secret;
    @Bean
    public Pbkdf2PasswordEncoder getPasswordEncoder (){
        return new Pbkdf2PasswordEncoder(secret, SALT_LENGTH, ITERATIONS, PBKDF2WithHmacSHA256);
    }
}

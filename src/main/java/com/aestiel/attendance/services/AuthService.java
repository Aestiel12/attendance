package com.aestiel.attendance.services;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.springframework.cglib.core.internal.Function;

import java.util.Date;

public interface AuthService {
    Cookie createAuthCookie(String email, String password) throws Exception;

    String generateAuthToken(String email, String password) throws Exception;

    boolean validateToken(String token);

    Claims getAllClaimsFromToken(String token);

    Date getExpDateFromToken(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);
}

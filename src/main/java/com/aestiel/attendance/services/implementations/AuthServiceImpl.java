package com.aestiel.attendance.services.implementations;

import com.aestiel.attendance.models.User;
import com.aestiel.attendance.services.AuthService;
import com.aestiel.attendance.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService { private static final int AUTH_COOKIE_TTL = 30 * 24 * 60 * 60;
    private static final int KEY_LENGTH = 32;

    @Value("${attendance.jwt.secret}")
    private String jwtSecret;

    @Value("${attendance.authCookieName}")
    private String authCookieName;
    private UserService userService;
    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Cookie createAuthCookie(String email, String password) throws Exception {
        String token = generateAuthToken(email, password);
        Cookie cookieTtl = new Cookie(authCookieName, token);
        cookieTtl.setPath("/");
        cookieTtl.setHttpOnly(true);
        cookieTtl.setMaxAge(AUTH_COOKIE_TTL);
        return cookieTtl;
    }

    @Override
    public String generateAuthToken(String email, String password) throws Exception {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            throw new Exception("Please provide a valid email address and password.");
        }

        User user = userService.findByEmail(email);
        if (user == null || !userService.isValidPassword(user, password)) {
            throw new Exception("Incorrect password.");
        }

        return generateJWT(new UserDetailsImpl(user.getId(), user.getEmail()));
    }

    public String generateJWT(UserDetailsImpl user) {
        Date now = new Date();
        Date jwtTokenExp = new Date(now.getTime() + AUTH_COOKIE_TTL * 1000L);
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyPlusPadding());

        return Jwts.builder()
                .subject(user.getEmail())
                .id(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiration(jwtTokenExp)
                .issuer("/")
                .signWith(secretKey)
                .compact();
    }

    private byte[] secretKeyPlusPadding() {
        byte[] key = jwtSecret.getBytes();
        byte[] result = new byte[KEY_LENGTH];
        System.arraycopy(key, 0, result, 0, key.length);

        for (int j = key.length; j < KEY_LENGTH; j++) {
            result[j] = 0;
        }

        return result;
    }


    @Override
    public boolean validateToken(String token) {
        Date expDate = getExpDateFromToken(token);
        Date now = new Date();
        return now.before(expDate);
    }

    @Override
    public Claims getAllClaimsFromToken(String token) {
        if (!StringUtils.hasText(token)){
            return null;
        }
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyPlusPadding());
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .decryptWith(secretKey)
                .build()
                .parseSignedClaims(token);

        return claims.getPayload();
    }

    @Override
    public Date getExpDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

}

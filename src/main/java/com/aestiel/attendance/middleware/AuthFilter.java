package com.aestiel.attendance.middleware;

import com.aestiel.attendance.services.AuthService;
import com.aestiel.attendance.services.UserService;
import com.aestiel.attendance.services.implementations.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AuthFilter extends OncePerRequestFilter {
    @Value("${attendance.authCookieName}")
    private String jwtCookie;

    private final AuthService authService;
    private final UserService userService;
    public AuthFilter(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

            Cookie[] cookies = request.getCookies();
            Claims claims = null;

            if (cookies != null) {
                Optional<Cookie> cookie = Arrays.stream(cookies)
                        .filter(name -> name.getName().equals(jwtCookie))
                        .findFirst();

                if (cookie.isPresent()) {
                    String token = cookie.get().getValue();
                    if (authService.validateToken(token)) {
                        claims = authService.getAllClaimsFromToken(token);
                    }
                }
            }

            String userEmail = null;
            if (claims != null) {
                userEmail = claims.get("sub", String.class);
            }

            if (StringUtils.hasText(userEmail)
                    && SecurityContextHolder.getContext().getAuthentication() == null && userService.existsByEmail(userEmail)) {
                UserDetailsImpl user = UserDetailsImpl.fromClaims(claims);
                WebAuthenticationDetails details = new WebAuthenticationDetailsSource()
                        .buildDetails(request);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(new SimpleGrantedAuthority("user"))
                );

                authentication.setDetails(details);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
    }
}

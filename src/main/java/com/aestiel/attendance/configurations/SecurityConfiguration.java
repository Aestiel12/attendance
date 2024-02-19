package com.aestiel.attendance.configurations;

import com.aestiel.attendance.middleware.AuthFilter;
import com.aestiel.attendance.services.AuthService;
import com.aestiel.attendance.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserService userService;
    private final AuthService authService;
    public SecurityConfiguration(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Bean
    public AuthFilter authenticationTokenFilterBean() {
        return new AuthFilter(authService, userService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        MvcRequestMatcher[] authWhitelist = {
                mvcMatcherBuilder.pattern("/api/register"),
                mvcMatcherBuilder.pattern("/api/login")
        };

        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(authWhitelist)
                        .permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/**"))
                        .authenticated()
                        .anyRequest()
                        .permitAll()
                )
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

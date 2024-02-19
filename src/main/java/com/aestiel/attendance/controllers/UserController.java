package com.aestiel.attendance.controllers;

import com.aestiel.attendance.services.AuthService;
import com.aestiel.attendance.services.UserService;
import com.aestiel.attendance.services.implementations.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestPart(value = "email", required = false) String email,
                                          @RequestPart(value = "password", required = false) String password)
            throws Exception {
        userService.validateNewUser(email, password);

        if (userService.existsByEmail(email)) {
            throw new Exception("User with this email already exists.");

        }

        userService.createUser(email, password);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Void> authenticateUser(@RequestPart(value = "email", required = false) String email,
                                                 @RequestPart(value = "password", required = false) String password,
                                                 HttpServletResponse response)
            throws Exception {
        response.addCookie(authService.createAuthCookie(email, password));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user/sign-out")
    public ResponseEntity<?> signOutUser(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        userService.removeAuthCookie(response);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        userService.deleteUser(userDetails.getId());
        userService.removeAuthCookie(response);
        return ResponseEntity.status(204).build();
    }
}

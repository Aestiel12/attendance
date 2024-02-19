package com.aestiel.attendance.controllers;

import com.aestiel.attendance.services.AuthService;
import com.aestiel.attendance.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        //this.userId = userService.findByEmail(email).getId();
        //this.loggedUserDetails = new UserDetailsImpl(userId,email);

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
}

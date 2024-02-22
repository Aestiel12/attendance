package com.aestiel.attendance.services;

import com.aestiel.attendance.exceptions.ValidationAppException;
import com.aestiel.attendance.models.User;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    boolean isCorrectEmailFormat(String email);

    boolean isValidPassword(User user, String password);

    User findByEmail(String email);

    User findById(Long id);

    void createUser(String email, String password);

    void validateNewUser(String email, String password) throws ValidationAppException;

    void removeAuthCookie(HttpServletResponse response);

    void deleteUser(Long id);

    String passwordToUserToken(String password);

    boolean isCorrectPasswordFormat(String password);

    boolean existsByEmail(String email);
}

package com.aestiel.attendance.services;

import com.aestiel.attendance.models.User;

public interface UserService {
    boolean isCorrectEmailFormat(String email);

    boolean isValidPassword(User user, String password);

    User findByEmail(String email);

    void createUser(String email, String password);

    void validateNewUser(String email, String password) throws Exception;

    String passwordToUserToken(String password);

    boolean isCorrectPasswordFormat(String password);

    boolean existsByEmail(String email);
}

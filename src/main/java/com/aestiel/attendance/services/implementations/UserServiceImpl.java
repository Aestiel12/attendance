package com.aestiel.attendance.services.implementations;

import com.aestiel.attendance.DTOs.Waypoint.EmailDTOWaypointApi;
import com.aestiel.attendance.exceptions.ValidationAppException;
import com.aestiel.attendance.models.User;
import com.aestiel.attendance.proxies.WaypointApi;
import com.aestiel.attendance.repositories.UserRepository;
import com.aestiel.attendance.services.UserService;
import com.aestiel.attendance.services.WaypointEmailService;
import com.aestiel.attendance.services.WorkService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder;
    private final WorkService workService;
    private final WaypointEmailService waypointEmailService;
    private final WaypointApi waypointApi;

    public UserServiceImpl(UserRepository userRepository, Pbkdf2PasswordEncoder pbkdf2PasswordEncoder, WorkService workService, WaypointEmailService waypointEmailService, WaypointApi waypointApi) {
        this.userRepository = userRepository;
        this.pbkdf2PasswordEncoder = pbkdf2PasswordEncoder;
        this.workService = workService;
        this.waypointEmailService = waypointEmailService;
        this.waypointApi = waypointApi;
    }

    @Value("${AUTH_COOKIE_NAME}")
    private String authCookieName;
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public void createUser(String email, String password) throws IOException {
        userRepository.save(new User(email, this.passwordToUserToken(password)));
        User user = userRepository.findUserByEmail(email);

        workService.createBasicActivities(user);

        EmailDTOWaypointApi emailDTO = waypointEmailService.createWelcomeEmail("wptemplate_H6XE7CSa6ZJCKsjQ", user);
        waypointApi.sendEmail(emailDTO).execute();
    }

    @Override
    public void validateNewUser(String email, String password) throws ValidationAppException {
        if (!isCorrectEmailFormat(email)) {
            throw new ValidationAppException("Invalid E-mail address format.");
        }

        if (!isCorrectPasswordFormat(password)) {
            throw new ValidationAppException("Invalid Password format.");
        }
    }

    @Override
    public void removeAuthCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(authCookieName, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public String passwordToUserToken(String password) {
        return pbkdf2PasswordEncoder.encode(password);
    }

    @Override
    public boolean isCorrectEmailFormat(String email){
        if (!StringUtils.hasText(email)) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email.trim());
        return matcher.matches();
    }

    @Override
    public boolean isValidPassword(User user, String password) {
        return pbkdf2PasswordEncoder.matches(password, user.getToken());
    }

    @Override
    public boolean isCorrectPasswordFormat(String password) {
        if (!StringUtils.hasText(password)) {
            return false;
        }
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
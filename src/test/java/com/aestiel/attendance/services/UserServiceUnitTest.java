package com.aestiel.attendance.services;

import com.aestiel.attendance.proxies.WaypointApi;
import com.aestiel.attendance.repositories.UserRepository;
import com.aestiel.attendance.services.implementations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceUnitTest {
    private UserRepository userRepository;

    @MockBean
    private Pbkdf2PasswordEncoder pbkdf2;
    private WorkService workService;
    private WaypointEmailService waypointEmailService;
    private WaypointApi waypointApi;

    @BeforeEach
    void configure() {
        userRepository = mock(UserRepository.class);
        pbkdf2 = mock();
        workService = mock(WorkService.class);
        waypointEmailService = mock(WaypointEmailService.class);
        waypointApi = mock(WaypointApi.class);
    }

    private static Stream<Arguments> provideDataForCheckEmailValidityReturnsCorrectValueTests() {
        return Stream.of(
                Arguments.of("123@123.ab", true),
                Arguments.of("   123@123.abc   ", true),
                Arguments.of("as@as.cz", true),
                Arguments.of("asd@asd.cz", true),
                Arguments.of("   as@as.abcd   ", true),
                Arguments.of("   ASD@AS.AB   ", true),
                Arguments.of("   123@123cz   ", false),
                Arguments.of("123 23.cz", false),
                Arguments.of("12323.cz", false),
                Arguments.of("      ", false),
                Arguments.of(null, false)
        );
    }
    @ParameterizedTest
    @MethodSource("provideDataForCheckEmailValidityReturnsCorrectValueTests")
    public void checkEmailValidityReturnsCorrectValue(String email, boolean expected){
        UserService userService = new UserServiceImpl(
                userRepository,
                pbkdf2,
                workService,
                waypointEmailService,
                waypointApi
        );

        assertEquals(expected, userService.isCorrectEmailFormat(email));
    }

    private static Stream<Arguments> provideDataForPasswordStrengthReturnsCorrectValueTests() {
        return Stream.of(
                Arguments.of("123", false),
                Arguments.of("   123", false),
                Arguments.of("abc123", false),
                Arguments.of("ABCabc123",false),
                Arguments.of(" abc123 ", false),
                Arguments.of(" ABCabc123 ",false),
                Arguments.of("", false),
                Arguments.of("      ", false),
                Arguments.of("ABab12*", false),
                Arguments.of("ABab12?*", true),
                Arguments.of("ABab12*?!*  ", false),
                Arguments.of(null, false)
        );
    }
    @ParameterizedTest
    @MethodSource("provideDataForPasswordStrengthReturnsCorrectValueTests")
    public void checkPasswordStrengthReturnsCorrectValue(String password, boolean expected){
        UserService userService = new UserServiceImpl(
                userRepository,
                pbkdf2,
                workService,
                waypointEmailService,
                waypointApi
        );

        assertEquals(expected, userService.isCorrectPasswordFormat(password));
    }

    @Test
    public void returnUserTokenReturnsEncryptedPassword() {
        when(pbkdf2.encode("password"))
                .thenReturn("encodedPassword");

        UserService userService = new UserServiceImpl(
                userRepository,
                pbkdf2,
                workService,
                waypointEmailService,
                waypointApi
        );

        String encodedPassword =  userService.passwordToUserToken("password");

        Mockito.verify(pbkdf2).encode("password");
        assertEquals("encodedPassword", encodedPassword);
        assertNotEquals("password", encodedPassword);
    }
}
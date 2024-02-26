package com.aestiel.attendance.controllers;

import com.aestiel.attendance.exceptions.ValidationAppException;
import com.aestiel.attendance.services.AuthService;
import com.aestiel.attendance.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.stream.Stream;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @Test
    public void registerUserReturnsCorrectResponseOnValidInput() throws Exception {
        MockPart emailPart = new MockPart("email", "asd@asd.cz".getBytes());
        MockPart passwordPart = new MockPart("password", "Asd*123".getBytes());

        when(userService.isCorrectEmailFormat("asd@asd.cz")).thenReturn(true);
        when(userService.isCorrectPasswordFormat("Asd*123")).thenReturn(true);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .multipart("/api/register")
                                .part(emailPart, passwordPart)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }

    private static Stream<Arguments> provideDataForRegisterUserReturnsCorrectStatusOnInvalidUser() {
        return Stream.of(
                Arguments.of("asdasd.cz", "123456"),
                Arguments.of("   asdasd.cz  ", "123456"),
                Arguments.of(" ", "123456"),
                Arguments.of("asd", " ")
        );
    }
    @ParameterizedTest
    @MethodSource("provideDataForRegisterUserReturnsCorrectStatusOnInvalidUser")
    public void registerUserReturnsCorrectStatusOnInvalidUser(String email, String password) throws Exception {
        MockPart emailPart = new MockPart("email", email.getBytes());
        MockPart passwordPart = new MockPart("password", password.getBytes());

        doThrow(new ValidationAppException("Test exception.")).when(userService)
                .validateNewUser(email, password);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .multipart("/api/register")
                                .part(emailPart, passwordPart)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Test exception."));

    }
}
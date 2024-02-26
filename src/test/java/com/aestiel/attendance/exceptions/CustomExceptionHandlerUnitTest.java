package com.aestiel.attendance.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;

public class CustomExceptionHandlerUnitTest {
    private static Stream<Arguments> provideDataForGetExceptionStatusReturnsCorrectValueTests() {
        return Stream.of(
                Arguments.of(new ValidationAppException(), 400),
                Arguments.of(new NotFoundAppException(), 404),
                Arguments.of(new UnauthorizedAppException(), 403),
                Arguments.of(new IOException(), 500),
                Arguments.of(new RuntimeException(), 500)
        );
    }
    @ParameterizedTest
    @MethodSource("provideDataForGetExceptionStatusReturnsCorrectValueTests")
    public void getExceptionStatusReturnsCorrectValue(Exception e, int expected){
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();

        assertEquals(expected, customExceptionHandler.getExceptionStatus(e));
    }

    private static Stream<Arguments> provideDataForHandleExceptionsReturnsCorrectValueTests() {
        return Stream.of(
                Arguments.of(new ValidationAppException("400 error message"), new ErrorResponseDTO("400 error message"), HttpStatus.valueOf(400)),
                Arguments.of(new NotFoundAppException("404 error message"), new ErrorResponseDTO("404 error message"), HttpStatus.valueOf(404)),
                Arguments.of(new UnauthorizedAppException("403 error message"), new ErrorResponseDTO("403 error message"), HttpStatus.valueOf(403)),
                Arguments.of(new IOException(), new ErrorResponseDTO(), HttpStatus.valueOf(500)),
                Arguments.of(new RuntimeException(), new ErrorResponseDTO(), HttpStatus.valueOf(500))
        );
    }
    @ParameterizedTest
    @MethodSource("provideDataForHandleExceptionsReturnsCorrectValueTests")
    public void handleExceptionsReturnsCorrectValue(Exception e, ErrorResponseDTO expectedMessage, HttpStatus expectedStatus){
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();

        ErrorResponseDTO body = (ErrorResponseDTO) customExceptionHandler.handleExceptions(e).getBody();

        assertEquals(expectedMessage.getMessage(), body.getMessage());
    }

    @Test
    public void loggerIsCalledOnExceptionReturning500StatusCode(){
        Logger logger = Mockito.mock(Logger.class);

        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler(logger);

        Exception e = new RuntimeException();

        customExceptionHandler.handleExceptions(e);

        Mockito.verify(logger).error("{} ERROR {}, Message: {}, Stack trace: {}",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                e.getClass(),
                e.getMessage(),
                e.getStackTrace()
        );
    }

    @Test
    public void loggerIsNotCalledOnExceptionReturning404StatusCode(){
        Logger logger = Mockito.mock(Logger.class);

        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler(logger);

        Exception e = new NotFoundAppException();

        customExceptionHandler.handleExceptions(e);

        Mockito.verify(logger, never()).error("{} ERROR {}, Message: {}, Stack trace: {}",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                e.getClass(),
                e.getMessage(),
                e.getStackTrace()
        );
    }
}
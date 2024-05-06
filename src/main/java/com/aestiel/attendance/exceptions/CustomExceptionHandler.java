package com.aestiel.attendance.exceptions;

import com.aestiel.attendance.annotations.ExceptionStatusCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final Logger logger;
    public CustomExceptionHandler() {
        this(LoggerFactory.getLogger(CustomExceptionHandler.class));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleExceptions(Exception e) {
        int statusCode = getExceptionStatus(e);

        if (statusCode == 900) {
            logger.error("{} ERROR {}, Message: {}, Stack trace: {}",
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                    e.getClass(),
                    e.getMessage(),
                    e.getStackTrace()
            );
        }

        return new ResponseEntity<>(new ErrorResponseDTO(e.getMessage()), HttpStatus.valueOf(statusCode));
    }

    int getExceptionStatus(Exception e){
        Class<? extends Exception> exceptionClass = e.getClass();
        if (exceptionClass.isAnnotationPresent(ExceptionStatusCode.class)) {
            return exceptionClass.getAnnotation(ExceptionStatusCode.class).status();
        } else {
            return 900;
        }
    }
}

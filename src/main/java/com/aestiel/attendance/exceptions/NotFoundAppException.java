package com.aestiel.attendance.exceptions;

import com.aestiel.attendance.annotations.ExceptionStatusCode;

@ExceptionStatusCode(status = 404)
public class NotFoundAppException  extends RuntimeException {
    public NotFoundAppException() {
        super();
    }
    public NotFoundAppException(String message) {
        super(message);
    }
    public NotFoundAppException(String message, Exception innerException) {
        super(message, innerException);
    }
}
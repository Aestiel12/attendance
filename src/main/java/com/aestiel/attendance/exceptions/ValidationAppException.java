package com.aestiel.attendance.exceptions;

import com.aestiel.attendance.annotations.ExceptionStatusCode;

@ExceptionStatusCode(status = 400)
public class ValidationAppException extends Exception {

    public ValidationAppException(){
        super();
    }
    public ValidationAppException(String message){
        super(message);
    }
    public ValidationAppException(String message, Exception innerException) {
        super(message, innerException);
    }

    public ValidationAppException(String message, IllegalArgumentException innerException){
        super(message, innerException);
    }
    public ValidationAppException(String message, NumberFormatException innerException){
        super(message, innerException);
    }
}

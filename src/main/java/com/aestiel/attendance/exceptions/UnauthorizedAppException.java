package com.aestiel.attendance.exceptions;

import com.aestiel.attendance.annotations.ExceptionStatusCode;

@ExceptionStatusCode(status = 403)
public class UnauthorizedAppException extends Exception {

    public UnauthorizedAppException(){
        super();
    }
    public UnauthorizedAppException(String message){
        super(message);
    }
    public UnauthorizedAppException(String message, Exception innerException) {
        super(message, innerException);
    }
}

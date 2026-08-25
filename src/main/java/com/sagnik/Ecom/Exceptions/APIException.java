package com.sagnik.Ecom.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class APIException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    /** Creates an API exception without a message. */
    public APIException() {
    }

    /** Creates an API exception with the supplied client-facing message. */
    public APIException(String message) {
        super(message);
    }






}

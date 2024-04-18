package com.dacm.taskManager.exception;


public class AuthErrorResponse extends RuntimeException {

    public AuthErrorResponse(String message) {
        super(message);
    }

    public AuthErrorResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthErrorResponse(Throwable cause) {
        super(cause);
    }
}


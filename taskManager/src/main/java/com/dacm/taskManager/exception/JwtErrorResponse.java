package com.dacm.taskManager.exception;

public class JwtErrorResponse extends RuntimeException{

    public JwtErrorResponse(String message) {
        super(message);
    }
    public JwtErrorResponse(String message, Throwable cause) {
        super(message, cause);
    }
    public JwtErrorResponse(Throwable cause) {
        super(cause);
    }
}

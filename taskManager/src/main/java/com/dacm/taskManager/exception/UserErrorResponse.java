package com.dacm.taskManager.exception;

public class UserErrorResponse extends RuntimeException{

    public UserErrorResponse(String message) {
        super(message);
    }

    public UserErrorResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public UserErrorResponse(Throwable cause) {
        super(cause);
    }

}

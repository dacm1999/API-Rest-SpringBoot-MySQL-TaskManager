package com.dacm.taskManager.exception;

public class UserResponse extends RuntimeException{

    public UserResponse(String message) {
        super(message);
    }

    public UserResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public UserResponse(Throwable cause) {
        super(cause);
    }

}

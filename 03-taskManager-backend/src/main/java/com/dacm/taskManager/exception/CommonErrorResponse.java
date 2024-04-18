package com.dacm.taskManager.exception;

public class CommonErrorResponse extends RuntimeException{

    public CommonErrorResponse(String message) {
        super(message);
    }

    public CommonErrorResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonErrorResponse(Throwable cause) {
        super(cause);
    }

}

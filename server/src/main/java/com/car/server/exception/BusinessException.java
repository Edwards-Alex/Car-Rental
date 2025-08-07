package com.car.server.exception;

public class BusinessException extends RuntimeException{

    private final Boolean success = false;
    private final String errorCode;

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Boolean getSuccess () {
        return success;
    }
}

package com.car.server.exception.enmu;

public enum ErrorCode {
    USER_NOT_FOUND("AUTH_001", "用户不存在"),
    INVALID_PASSWORD("AUTH_002", "密码错误"),
    ACCOUNT_LOCKED("AUTH_003", "账户已锁定"),
    INVALID_TOKEN("AUTH_004", "Token已失效");




    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

package com.car.server.exception;

public class ErrorResponse {

    private Boolean success;
    private String code;
    private String message;

    // 必须要有无参构造器（Jackson 需要）
    public ErrorResponse(){}

    public ErrorResponse(Boolean success,String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

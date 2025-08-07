package com.car.server.utility;

public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    private boolean success;



    // 错误响应（静态工厂方法）
    public static <T> ApiResponse<T> error(boolean success,String code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(success);
        response.setCode(code);
        response.setMessage(message);
        response.setData(null); // 错误时通常无数据
        return response;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

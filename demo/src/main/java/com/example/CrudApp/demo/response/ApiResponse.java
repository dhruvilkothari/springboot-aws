package com.example.CrudApp.demo.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Object error;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data, Object error) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    // getters & setters
}

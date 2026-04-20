package com.ghosted.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int status;

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data, 200);
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        return new ApiResponse<>(false, message, null, status);
    }

    public static <T> ApiResponse<T> error(String message, int status, T data) {
        return new ApiResponse<>(false, message, data, status);
    }
}

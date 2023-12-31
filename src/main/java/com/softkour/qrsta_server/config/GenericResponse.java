package com.softkour.qrsta_server.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class GenericResponse<T> {
    private boolean success;
    private Object messages;
    private int code;
    private T data;

    public static <T> ResponseEntity<GenericResponse<T>> empty() {
        return new ResponseEntity<>(
                GenericResponse.<T>builder()
                        .messages("Empty Response")
                        .data(null)
                        .code(Constants.ERROR_EMPTY)
                        .success(false)
                        .build(), HttpStatusCode.valueOf(Constants.ERROR_EMPTY));
    }

    public static <T> ResponseEntity<GenericResponse<T>> success(T data) {
        return ResponseEntity.ok(GenericResponse.<T>builder()
                .messages(null)
                .data(data)
                .code(200)
                .success(true)
                .build());
    }
    public static <String> ResponseEntity<GenericResponse<String>> successWithMessageOnly(String message) {
        return ResponseEntity.ok(GenericResponse.<String>builder()
                .messages(null)
                .data(message)
                .code(200)
                .success(true)
                .build());
    }
    public static <T> ResponseEntity<GenericResponse<T>> error(T error) {
        return new ResponseEntity<>(
                GenericResponse.<T>builder()
                        .messages(error)
                        .success(false)
                        .code(Constants.ERROR_CODE)
                        .data(null)
                        .build(), HttpStatusCode.valueOf(Constants.ERROR_CODE));
    }

    public static <T> ResponseEntity<GenericResponse<T>> errorWithCoder(T error,int code) {
        return new ResponseEntity<>(
                GenericResponse.<T>builder()
                        .messages(error)
                        .success(false)
                        .code(code)
                        .data(null)
                        .build(), HttpStatusCode.valueOf(code));
    }
}
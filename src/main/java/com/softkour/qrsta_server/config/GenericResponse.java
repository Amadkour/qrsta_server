package com.softkour.qrsta_server.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.softkour.qrsta_server.exception.GlobalExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@AllArgsConstructor
@Slf4j
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
                                                .build(),
                                HttpStatusCode.valueOf(Constants.ERROR_EMPTY));
        }

        public static <T> ResponseEntity<GenericResponse<T>> success(T data) {
                return ResponseEntity.ok(GenericResponse.<T>builder()
                                .messages(null)
                                .data(data)
                                .code(200)
                                .success(true)
                                .build());
        }

        public static <T> ResponseEntity<GenericResponse<Object>> successWithMessageOnly(String message) {
                Map<String, String> meMap = new HashMap<String, String>();
                meMap.put("message", message);
                return ResponseEntity.ok(GenericResponse.<Object>builder()
                                .messages(null)
                                .data(meMap)
                                .code(200)
                                .success(true)
                                .build());
        }

        public static <T> ResponseEntity<GenericResponse<Object>> errorWithMessageOnly(String message) {
                Map<String, String> errMap = new HashMap<String, String>();
                errMap.put("error", message);
                return new ResponseEntity<>(
                                GenericResponse.<Object>builder()
                                                .messages(errMap)
                                                .success(false)
                                                .code(Constants.ERROR_CODE)
                                                .data(null)
                                                .build(),
                                HttpStatusCode.valueOf(Constants.ERROR_CODE));
        }

        public static <T> ResponseEntity<GenericResponse<Object>> errorOfMap(Map<String, Object> errMap) {
                return new ResponseEntity<>(
                                GenericResponse.<Object>builder()
                                                .messages(errMap)
                                                .success(false)
                                                .code(Constants.ERROR_CODE)
                                                .data(null)
                                                .build(),
                                HttpStatusCode.valueOf(Constants.ERROR_CODE));
        }

        public static <T> ResponseEntity<GenericResponse<Object>> errorOfException(Exception error) {
                log.warn("Exception handler===================GenericResponse ");

                if (error.getClass().isInstance(DataIntegrityViolationException.class))
                        return new ResponseEntity<>(
                                        GenericResponse.<Object>builder()
                                                        .messages(GlobalExceptionHandler
                                                                        .processDataIntegrityViolationException(
                                                                                        (DataIntegrityViolationException) error))
                                                        .success(false)
                                                        .code(Constants.ERROR_CODE)
                                                        .data(null)
                                                        .build(),
                                        HttpStatusCode.valueOf(Constants.ERROR_CODE));
                return new ResponseEntity<>(
                                GenericResponse.<Object>builder()
                                                .messages(GlobalExceptionHandler.processErrors(error))
                                                .success(false)
                                                .code(Constants.ERROR_CODE)
                                                .data(null)
                                                .build(),
                                HttpStatusCode.valueOf(Constants.ERROR_CODE));
        }

        public static <T> ResponseEntity<GenericResponse<T>> errorWithCoder(T error, int code) {
                return new ResponseEntity<>(
                                GenericResponse.<T>builder()
                                                .messages(error)
                                                .success(false)
                                                .code(code)
                                                .data(null)
                                                .build(),
                                HttpStatusCode.valueOf(code));
        }

}
package com.softkour.qrsta_server.exception;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
//
//@ControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//                Map<String, List<String>> body = new HashMap<>();
//
//        List<String> errors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.toList());
//        body.put("errors", errors);
//         super.handleMethodArgumentNotValid(ex, headers, status, request);
//                return new ResponseEntity<>(
//                GenericResponse.<Map<String, List<String>> >builder()
//                        .messages(body)
//                        .success(false)
//                        .code(Constants.ERROR_CODE)
//                        .data(null)
//                        .build(), HttpStatusCode.valueOf(Constants.ERROR_CODE));
//
//    }
//
//
//    //
////    @Override
////    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
////                                                                  HttpStatus status, WebRequest request) {
////        Map<String, List<String>> body = new HashMap<>();
////
////        List<String> errors = ex.getBindingResult()
////                .getFieldErrors()
////                .stream()
////                .map(DefaultMessageSourceResolvable::getDefaultMessage)
////                .collect(Collectors.toList());
////
////        body.put("errors", errors);
////
////        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
////    }
//}

import com.softkour.qrsta_server.config.GenericResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RestController
@Configuration
@Slf4j
public class GlobalExceptionHandler {

    /// validation exceptions
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<GenericResponse<Object>> handleException(
            MethodArgumentNotValidException e) {
        log.warn("Exception handler===================validation ");
        return GenericResponse.errorOfMap(processvalidationErrors(e));
    }

    /// client exceptions
    @ExceptionHandler(value = ClientException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<GenericResponse<Object>> handingRuntimeException(
            ClientException exception) {

        Map<String, Object> errMap = new HashMap<String, Object>();
        if (exception.code != null) {
            return GenericResponse.errorWithCoder("update app version", exception.code);
        }
        errMap.put(exception.key, exception.getLocalizedMessage());
        return GenericResponse.errorOfMap(errMap);
    }

    /// database constraint violation
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<GenericResponse<Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {
        return GenericResponse.errorOfException(exception);
    }

    public static Map<String, Object> processErrors(Exception e) {
        Map<String, Object> validationErrorModels = new HashMap<String, Object>();
        validationErrorModels.put("error", e.getLocalizedMessage());

        return validationErrorModels;
    }

    public static Map<String, String> processDataIntegrityViolationException(DataIntegrityViolationException e) {
        Map<String, String> validationErrorModels = new HashMap<String, String>();
        String constraintName = ((ConstraintViolationException) e.getCause()).getConstraintName();

        validationErrorModels.put(constraintName, e.getMostSpecificCause().getMessage());

        return validationErrorModels;
    }

    private Map<String, Object> processvalidationErrors(MethodArgumentNotValidException e) {
        Map<String, Object> validationErrorModels = new HashMap<String, Object>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            validationErrorModels.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return validationErrorModels;
    }
}
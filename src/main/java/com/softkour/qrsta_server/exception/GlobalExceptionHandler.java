package com.softkour.qrsta_server.exception;
import java.util.ArrayList;
import java.util.List;

import com.softkour.qrsta_server.config.GenericResponse;
import org.springframework.context.annotation.Configuration;
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

@ControllerAdvice
@RestController
@Configuration
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<GenericResponse<List<ErrorModel>>> handleException(MethodArgumentNotValidException e) {
        List<ErrorModel> errorModels = processErrors(e);
        return GenericResponse.error(errorModels);
    }

    private List<ErrorModel> processErrors(MethodArgumentNotValidException e) {
        List<ErrorModel> validationErrorModels = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            ErrorModel validationErrorModel = ErrorModel
                    .builder()
                    .code(fieldError.getCode())
//                    .source(fieldError.getObjectName() + "/" + fieldError.getField())
                    .source( fieldError.getField())
                    .detail(fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .build();
            validationErrorModels.add(validationErrorModel);
        }
        return validationErrorModels;
    }
}
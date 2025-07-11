package com.ecommerce.project.exceptions;


import com.ecommerce.project.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice

public class MyGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
       Map<String, String> response = new HashMap<>();
       e.getBindingResult().getAllErrors().forEach(err -> {
           String fieldName = ((FieldError)err).getField();
           String message = err.getDefaultMessage();
           response.put(fieldName, message);
       });
       return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> handleProductNotFound(ResourceNotFoundException ex) {
        APIResponse apiResponse = new APIResponse(ex.getMessage(),false );
        return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> handleProductNotFound(APIException ex) {
        APIResponse apiResponse = new APIResponse(ex.getMessage(),false );
        return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CategoryNotPresent.class)
    public ResponseEntity<String> handleProductNotFound(CategoryNotPresent ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}


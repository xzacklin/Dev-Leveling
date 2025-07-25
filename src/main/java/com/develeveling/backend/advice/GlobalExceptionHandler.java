package com.develeveling.backend.advice;

import com.develeveling.backend.exception.DuplicateUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Duplicate username / e‑mail
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicate(DuplicateUserException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)   // 409
                .body(ex.getMessage());
    }

    // Bean‑Validation fails
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();
        return ResponseEntity
                .badRequest()                   // 400
                .body(msg);
    }
}

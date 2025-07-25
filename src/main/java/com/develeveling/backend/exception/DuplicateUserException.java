package com.develeveling.backend.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String field) {
        super(field + " already exists");
    }
}

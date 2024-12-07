package com.assessment.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Custom exception classes
public class CustomException {
    public static class BadRequestException extends ResponseStatusException {
        public BadRequestException(String message) {
            super(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static class ServerErrorException extends ResponseStatusException {
        public ServerErrorException(String message) {
            super(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
    }
    public static class UserNotFoundException extends ResponseStatusException {
        public UserNotFoundException(String message) {
            super(HttpStatus.NOT_FOUND, message);
        }
    }
}

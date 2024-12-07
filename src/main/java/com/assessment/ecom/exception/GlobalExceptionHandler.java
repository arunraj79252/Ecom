package com.assessment.ecom.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER= LoggerFactory.getLogger(GlobalExceptionHandler.class);
        @ExceptionHandler(CustomException.BadRequestException.class)
        public ResponseEntity<String> handleBadRequestException(CustomException.BadRequestException e) {
            LOGGER.error(e.getMessage()+e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGeneralException(Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred!");
        }
    }


package org.sdi.usermanager.controller;

import org.sdi.usermanager.dto.ErrorDTO;
import org.sdi.usermanager.exception.EmailAlreadyExistsException;
import org.sdi.usermanager.exception.InvalidCredentialsException;
import org.sdi.usermanager.exception.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("Validation error");

        ErrorDTO error = ErrorDTO.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(message)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ErrorDTO error = ErrorDTO.builder()
                .status(String.valueOf(HttpStatus.CONFLICT))
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException ex) {
        ErrorDTO error = ErrorDTO.builder()
                .status(String.valueOf(HttpStatus.NOT_FOUND))
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDTO> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ErrorDTO error = ErrorDTO.builder()
                .status(String.valueOf(HttpStatus.UNAUTHORIZED))
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception ex) {
        ErrorDTO error = ErrorDTO.builder()
                .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

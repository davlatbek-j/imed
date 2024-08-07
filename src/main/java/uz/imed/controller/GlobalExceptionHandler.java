package uz.imed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import uz.imed.config.security.AuthorizationFailedException;
import uz.imed.exception.IllegalPhotoTypeException;
import uz.imed.exception.NoUniqueNameException;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalPhotoTypeException.class)
    public ResponseEntity<ApiResponse<?>> handlePhotoTypeException(IllegalPhotoTypeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Illegal photo: " + e.getMessage(), null));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleOtherException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(NoUniqueNameException.class)
    public ResponseEntity<ApiResponse<?>> handleNoUniqueNameException(NoUniqueNameException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthorizationFailedException(AuthorizationFailedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }
}

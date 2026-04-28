package com.blog.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException e,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = ErrorResponse.of(
                errorCode,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ErrorResponse response = ErrorResponse.of(
                ErrorCode.INVALID_INPUT,
                message,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e,
            HttpServletRequest request
    ) {
        ErrorResponse response = ErrorResponse.of(
                ErrorCode.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(response);
    }
}

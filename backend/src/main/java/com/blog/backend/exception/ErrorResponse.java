package com.blog.backend.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getStatus().value())
                .error(errorCode.getStatus().name())
                .message(errorCode.getMessage())
                .path(path)
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getStatus().value())
                .error(errorCode.getStatus().name())
                .message(message)
                .path(path)
                .build();
    }
}

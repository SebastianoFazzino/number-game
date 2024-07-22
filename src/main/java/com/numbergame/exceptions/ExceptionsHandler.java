package com.numbergame.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashSet;
import java.util.Set;

import static io.netty.handler.logging.LogLevel.ERROR;
import static io.netty.handler.logging.LogLevel.WARN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler  {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ExceptionResponse> handleGenericException(
            Exception exception) {

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse
                        .builder()
                        .message(exception.getMessage())
                        .errorCode("generic_exception")
                        .logLevel(ERROR)
                        .httpStatus(INTERNAL_SERVER_ERROR)
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleArgumentNotValid(
            MethodArgumentNotValidException exception) {

        Set<String> validationErrors = new HashSet<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            var errorMessage = error.getDefaultMessage();
            validationErrors.add(errorMessage);
        });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ExceptionResponse
                        .builder()
                        .message(exception.getMessage())
                        .errorCode("method_argument_not_valid")
                        .logLevel(WARN)
                        .httpStatus(BAD_REQUEST)
                        .validationErrors(validationErrors)
                        .build()
                );
    }

}

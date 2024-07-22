package com.numbergame.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.netty.handler.logging.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    private HttpStatus httpStatus;

    private String errorCode;

    private String message;

    private LogLevel logLevel;

    private Set<String> validationErrors;

    private final Date dateTime = new Date(System.currentTimeMillis());
}

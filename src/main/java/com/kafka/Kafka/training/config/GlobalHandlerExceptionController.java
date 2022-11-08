package com.kafka.Kafka.training.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalHandlerExceptionController {

    @ResponseBody
    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<String> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception) {

        final String errorLog = exception.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
        log.error("MethodArgumentNotValidException: {}", errorLog);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = {
            BindException.class
    })
    public ResponseEntity<String> handleBindException(
            final BindException exception) {

        final String errorLog = exception.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
        log.error("BindException: {}", errorLog);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = {
            Exception.class
    })
    public ResponseEntity<String> handleGeneralException(final Exception exception) {
        log.error("GeneralException: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}

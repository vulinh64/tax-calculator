package com.vulinh.exception;

import com.vulinh.model.GeneralResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<GeneralResponse<Object>> handleValidationException(
      ValidationException validationException) {
    var isInternalError = Objects.nonNull(validationException.getCause());

    if (isInternalError) {
      log.info("Internal error", validationException);
    }

    return ResponseEntity.status(
            isInternalError ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.UNAUTHORIZED)
        .body(GeneralResponse.error(validationException.errorResult()));
  }
}

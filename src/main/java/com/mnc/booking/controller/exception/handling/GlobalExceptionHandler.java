package com.mnc.booking.controller.exception.handling;

import com.mnc.booking.controller.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final String ILLEGAL_ARG_EX_MESSAGE = "put message here";

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDTO> handleDataParsingException(final IllegalArgumentException exception) {

    final ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
        .code(HttpStatus.BAD_REQUEST.value())
        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .timestamp(Instant.now())
        .message(exception.getMessage())
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

}

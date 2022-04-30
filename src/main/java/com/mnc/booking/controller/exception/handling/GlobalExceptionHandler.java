package com.mnc.booking.controller.exception.handling;

import com.mnc.booking.controller.dto.ErrorResponseDTO;
import com.mnc.booking.exception.AlreadyExistsException;
import com.mnc.booking.exception.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final String VALIDATION_ERROR_MSG = "There is a validation error. Please check the details.";

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

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ErrorResponseDTO> handleDataParsingException(final AlreadyExistsException exception) {

    final ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
        .code(HttpStatus.CONFLICT.value())
        .status(HttpStatus.CONFLICT.getReasonPhrase())
        .timestamp(Instant.now())
        .message(exception.getMessage())
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleDataParsingException(final NotFoundException exception) {

    final ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
        .code(HttpStatus.NOT_FOUND.value())
        .status(HttpStatus.NOT_FOUND.getReasonPhrase())
        .timestamp(Instant.now())
        .message(exception.getMessage())
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {
    final ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
        .code(HttpStatus.BAD_REQUEST.value())
        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .timestamp(Instant.now())
        .message(VALIDATION_ERROR_MSG)
        .details(getDetails(ex))
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  private List<String> getDetails(final MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());
  }


}

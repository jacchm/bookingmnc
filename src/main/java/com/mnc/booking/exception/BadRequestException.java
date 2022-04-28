package com.mnc.booking.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends DomainException {

  private static final HttpStatus status = HttpStatus.BAD_REQUEST;

  public BadRequestException(String message) {
    super(status, message);
  }
}

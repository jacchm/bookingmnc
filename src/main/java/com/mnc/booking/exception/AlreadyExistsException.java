package com.mnc.booking.exception;

public class AlreadyExistsException extends RuntimeException {

  public AlreadyExistsException(final String message) {
    super(message);
  }

}

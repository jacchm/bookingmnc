package com.mnc.booking.controller.dto.user;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserFilterParams {

  private String username;
  private String email;
  private String name;
  private String surname;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  // TODO: check what type of exception is thrown here
  private LocalDate dateOfBirth;
  private String role;
  private String phoneNumber;
}


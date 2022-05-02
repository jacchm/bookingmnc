package com.mnc.booking.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserFilterParams {

  private String username;
  private String name;
  private String surname;
  private String email;
  private String authorities;
}

package com.mnc.booking.controller.dto.user;

import lombok.Data;

@Data
public class UserUpdateDTO {

  private String email;
  private String name;
  private String surname;
  private String dateOfBirth;
  private String phoneNumber;
  private String photoURI;
}

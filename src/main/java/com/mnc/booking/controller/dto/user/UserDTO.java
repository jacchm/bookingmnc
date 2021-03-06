package com.mnc.booking.controller.dto.user;

import lombok.Data;

@Data
public class UserDTO {

  private String username;
  private String email;
  private String name;
  private String surname;
  private String dateOfBirth;
  private String authorities;
  private String phoneNumber;
  private String photoURI;

}

package com.mnc.booking.controller.dto.user;

import lombok.Data;

@Data
public class UserPasswordUpdateDTO {

  private String password;
  private String newPassword;

}

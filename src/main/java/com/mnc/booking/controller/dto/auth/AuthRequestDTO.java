package com.mnc.booking.controller.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {

  @NotBlank(message = "Username cannot be null.")
  private String username;

  @NotBlank(message = "Password cannot be null.")
  private String password;

}

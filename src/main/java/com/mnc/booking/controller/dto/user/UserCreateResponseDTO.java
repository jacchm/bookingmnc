package com.mnc.booking.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(staticName = "of")
@Data
public class UserCreateResponseDTO {
  private String username;
}

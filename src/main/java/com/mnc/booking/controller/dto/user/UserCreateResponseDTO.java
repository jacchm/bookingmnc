package com.mnc.booking.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Data
public class UserCreateResponseDTO {
  private String username;
}

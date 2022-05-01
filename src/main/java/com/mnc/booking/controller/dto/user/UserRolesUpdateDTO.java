package com.mnc.booking.controller.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class UserRolesUpdateDTO {
  @JsonAlias(value = "roles")
  private List<String> authorities;
}

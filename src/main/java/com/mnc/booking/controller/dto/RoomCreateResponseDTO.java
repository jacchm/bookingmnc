package com.mnc.booking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(staticName = "of")
@Data
public class RoomCreateResponseDTO {
  private String roomNo;
}

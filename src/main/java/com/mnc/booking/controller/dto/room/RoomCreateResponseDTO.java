package com.mnc.booking.controller.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Data
public class RoomCreateResponseDTO {
  private String roomNo;
}

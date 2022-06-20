package com.mnc.booking.controller.dto.room;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoomSearchParams extends RoomFilterParams {

  private Instant availableFrom;
  private Instant availableTo;

}

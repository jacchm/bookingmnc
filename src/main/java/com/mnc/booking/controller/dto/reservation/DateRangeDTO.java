package com.mnc.booking.controller.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
public class DateRangeDTO {

  private Instant from;
  private Instant to;

}

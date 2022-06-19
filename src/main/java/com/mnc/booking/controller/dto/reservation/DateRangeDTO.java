package com.mnc.booking.controller.dto.reservation;

import lombok.Data;

import java.time.Instant;

@Data
public class DateRangeDTO {

  private Instant from;
  private Instant to;

}

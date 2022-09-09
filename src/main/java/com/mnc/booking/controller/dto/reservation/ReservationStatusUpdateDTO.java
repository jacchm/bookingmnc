package com.mnc.booking.controller.dto.reservation;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class ReservationStatusUpdateDTO {

  @Pattern(regexp = "PAID|ACCEPTED|REJECTED")
  private String status;

}

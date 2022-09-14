package com.mnc.booking.controller.dto.reservation;

import com.mnc.booking.controller.dto.validation.ReservationCreation;
import com.mnc.booking.controller.dto.validation.ValidDateRange;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class ReservationCreationDTO {

  @NotBlank(message = "roomNo must be specified.")
  private String roomNo;
  @NotBlank(message = "username must be specified.", groups = ReservationCreation.class)
  private String username;
  @Min(value = 1, message = "noPeople must be specified and greater than or equal to 1.")
  private int noPeople;
  @ValidDateRange
  private DateRangeDTO dateRange;
  private boolean breakfastIncluded;
  private boolean dinnerIncluded;
  private boolean supperIncluded;
  private boolean parkingIncluded;
  private boolean animalsIncluded;

}

package com.mnc.booking.controller.dto.reservation;

import com.mnc.booking.model.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ReservationFilterParams {

  private String roomNo;
  private String username;
  private Integer noPeople;
  private Boolean breakfastIncluded;
  private Boolean dinnerIncluded;
  private Boolean supperIncluded;
  private Boolean parkingIncluded;
  private Boolean animalsIncluded;
  private Instant activeDate;
  private BigDecimal minTotalCostValue;
  private BigDecimal maxTotalCostValue;
  private ReservationStatus status;

}

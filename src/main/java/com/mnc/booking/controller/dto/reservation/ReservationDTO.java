package com.mnc.booking.controller.dto.reservation;

import com.mnc.booking.model.ReservationStatus;

import java.math.BigDecimal;

public class ReservationDTO {

  private Long id;
  private String roomNo;
  private String username;
  private int noPeople;
  private boolean breakfastIncluded;
  private boolean dinnerIncluded;
  private boolean supperIncluded;
  private DateRangeDTO dateRange;
  private boolean parkingIncluded;
  private boolean animalsIncluded;
  private BigDecimal totalCostValue;
  private String totalCostCurrency;
  private ReservationStatus status;

}

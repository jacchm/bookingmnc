package com.mnc.booking.controller.dto.reservation;

import com.mnc.booking.controller.dto.room.PriceDTO;
import com.mnc.booking.model.ReservationStatus;
import lombok.Data;

@Data
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
  private PriceDTO totalCost;
  private ReservationStatus status;

}

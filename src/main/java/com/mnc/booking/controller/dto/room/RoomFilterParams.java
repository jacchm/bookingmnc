package com.mnc.booking.controller.dto.room;

import com.mnc.booking.model.BathroomType;
import com.mnc.booking.model.RoomType;
import com.mnc.booking.model.Status;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomFilterParams {

  private Integer noPeople;
  private RoomType roomType;
  private BigDecimal pricePerNightValue;
  private String pricePerNightCurrency;
  private Boolean isBalcony;
  private Boolean isOutstandingView;
  private Boolean isTv;
  private BathroomType bathroomType;
  private Boolean isCoffeeMachine;
  private Boolean isRestArea;
  private Status status;
  private Integer roomSizeValue;

}

package com.mnc.booking.controller.dto.room;

import com.mnc.booking.model.BathroomType;
import com.mnc.booking.model.Price;
import com.mnc.booking.model.RoomType;
import com.mnc.booking.model.Status;
import lombok.Data;

@Data
public class RoomFilterParams {

  private String roomNo;
  private Integer noPeople;
  private String description;
  private RoomType roomType;
  private Price pricePerNight;
  private Boolean isBalcony;
  private Boolean isOutstandingView;
  private Boolean isTv;
  private BathroomType bathroomType;
  private Boolean isCoffeeMachine;
  private Boolean isRestArea;
  //  private MeasurementUnit roomSize;
  private Status status;

}

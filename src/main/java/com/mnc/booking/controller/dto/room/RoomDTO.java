package com.mnc.booking.controller.dto.room;

import com.mnc.booking.model.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class RoomDTO {

  private String roomNo;
  private int noPeople;
  private String description;
  private RoomType roomType;
  private Price pricePerNight;
  private Boolean isBalcony;
  private Boolean isOutstandingView;
  private Boolean isTv;
  private BathroomType bathroomType;
  private Boolean isCoffeeMachine;
  private Boolean isRestArea;
  private MeasurementUnit roomSize;
  private List<URI> images;
  private Status status;
  private int version;
  private Instant createdAt;
  private Instant modifiedAt;
}

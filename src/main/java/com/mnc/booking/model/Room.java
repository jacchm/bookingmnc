package com.mnc.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "rooms")
public class Room {

  @Id
  private String roomNo;
  private int noPeople;
  private String description;
  @Enumerated(EnumType.STRING)
  private RoomType roomType;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "value", column = @Column(name = "price_per_night_value")),
      @AttributeOverride(name = "currency", column = @Column(name = "price_per_night_currency"))
  })
  private Price pricePerNight;
  private Boolean isBalcony;
  private Boolean isOutstandingView;
  private Boolean isTv;
  @Enumerated(EnumType.STRING)
  private BathroomType bathroomType;
  private Boolean isCoffeeMachine;
  private Boolean isRestArea;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "value", column = @Column(name = "room_size_value")),
      @AttributeOverride(name = "unit", column = @Column(name = "room_size_currency"))
  })
  private MeasurementUnit roomSize;
  @OneToMany(mappedBy = "roomNo")
  private List<URI> images;
  @Enumerated(EnumType.STRING)
  private Status status;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "version", column = @Column(name = "room_version")),
      @AttributeOverride(name = "createdAt", column = @Column(name = "room_createdAt")),
      @AttributeOverride(name = "modifiedAt", column = @Column(name = "room_modifiedAt"))
  })
  private Metadata metadata;

}

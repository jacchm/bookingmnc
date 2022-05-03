package com.mnc.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
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
      @AttributeOverride(name = "value", column = @Column(name = "pricepernightvalue")),
      @AttributeOverride(name = "currency", column = @Column(name = "pricepernightcurrency"))
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
      @AttributeOverride(name = "value", column = @Column(name = "roomsizevalue")),
      @AttributeOverride(name = "unit", column = @Column(name = "roomsizecurrency"))
  })
  private MeasurementUnit roomSize;
  @OneToMany(mappedBy = "roomNo", cascade = CascadeType.ALL)
  private List<URI> images;
  @Enumerated(EnumType.STRING)
  private Status status;
  @Version
  private int version;
  @Column(updatable = false)
  private Instant createdAt;
  @UpdateTimestamp
  private Instant modifiedAt;

  @PrePersist
  public void prePersistCreatedAt() {
    this.createdAt = Instant.now();
  }

}

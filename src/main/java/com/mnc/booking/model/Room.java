package com.mnc.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "room")
public class Room {

  @Id
  private String roomNo;
  private int noPeople;
  private String description;
  @Enumerated(EnumType.STRING)
  private RoomType roomType;
  private BigDecimal pricePerNightValue;
  private String pricePerNightCurrency;
  private Boolean isBalcony;
  private Boolean isOutstandingView;
  private Boolean isTv;
  @Enumerated(EnumType.STRING)
  private BathroomType bathroomType;
  private Boolean isCoffeeMachine;
  private Boolean isRestArea;
  private Integer roomSizeValue;
  private String roomSizeUnit;
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

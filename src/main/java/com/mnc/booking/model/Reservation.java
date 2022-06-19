package com.mnc.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "reservation")
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String roomNo;
  private String username;
  private int noPeople;
  private boolean breakfastIncluded;
  private boolean dinnerIncluded;
  private boolean supperIncluded;
  private Instant dateFrom;
  private Instant dateTo;
  private boolean parkingIncluded;
  private boolean animalsIncluded;
  private BigDecimal totalCostValue;
  private String totalCostCurrency;
  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

}

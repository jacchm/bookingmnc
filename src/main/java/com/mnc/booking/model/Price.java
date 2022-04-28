package com.mnc.booking.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Data
@Embeddable
public class Price {
  private BigDecimal value;
  private String currency;
}

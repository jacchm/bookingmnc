package com.mnc.booking.model;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class MeasurementUnit {
  private BigDecimal value;
  private String unit;
}

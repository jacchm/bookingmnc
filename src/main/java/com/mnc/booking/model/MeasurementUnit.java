package com.mnc.booking.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class MeasurementUnit {
  private Integer value;
  private String unit;
}

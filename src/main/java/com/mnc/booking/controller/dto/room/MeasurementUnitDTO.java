package com.mnc.booking.controller.dto.room;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class MeasurementUnitDTO {

  @Min(value = 1L, message = "roomSize value must be specified.")
  private int value;
  @NotNull(message = "roomSize unit must be specified.")
  private String unit;
}

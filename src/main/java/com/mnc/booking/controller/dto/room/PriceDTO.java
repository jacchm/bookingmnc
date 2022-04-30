package com.mnc.booking.controller.dto.room;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class PriceDTO {
  @Min(value = 1L, message = "price value must be specified.")
  private BigDecimal value;
  @NotNull(message = "price currency must be specified.")
  private String currency;
}

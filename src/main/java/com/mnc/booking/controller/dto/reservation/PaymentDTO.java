package com.mnc.booking.controller.dto.reservation;

import com.mnc.booking.controller.dto.PriceDTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class PaymentDTO {

  @Valid
  private final PriceDTO payment;
  @NotBlank(message = "cardNo must be specified.")
  private final String cardNo;
}

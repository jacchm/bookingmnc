package com.mnc.booking.controller.dto.reservation;

import com.mnc.booking.controller.dto.PriceDTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class PaymentDTO {

  @Valid
  private PriceDTO payment;
  @NotBlank(message = "cardNo must be specified.")
  private String cardNo;
  @NotBlank(message = "username must be specified.")
  private String username;
}

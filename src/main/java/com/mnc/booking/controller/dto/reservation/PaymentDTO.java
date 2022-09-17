package com.mnc.booking.controller.dto.reservation;

import com.mnc.booking.controller.dto.PriceDTO;
import com.mnc.booking.controller.dto.validation.ReservationControllerValidation;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class PaymentDTO {

  @Valid
  private PriceDTO payment;
  @NotBlank(message = "username must be specified.", groups = ReservationControllerValidation.class)
  private String username;
}

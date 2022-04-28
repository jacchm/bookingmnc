package com.mnc.booking.controller.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class RoomCreationDTO {

  @NotBlank(message = "roomNo must be specified.")
  private String roomNo;
  @Min(value = 1, message = "noPeople must be specified and greater than or equal to 1.")
  private int noPeople;
  @NotBlank(message = "description must be specified.")
  private String description;

}

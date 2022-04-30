package com.mnc.booking.controller.dto.room;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class URIDTO {

  @NotBlank(message = "URI roomNo must be specified.")
  private String roomNo;
  @NotBlank(message = "uri must be specified.")
  private String uri;
}


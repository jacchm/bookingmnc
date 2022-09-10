package com.mnc.booking.controller.dto.room;

import com.mnc.booking.controller.dto.validation.URICreation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@NoArgsConstructor
@Data
public class URIDTO {

  @NotBlank(message = "URI roomNo must be specified.", groups = {URICreation.class})
  private String roomNo;
  @NotBlank(message = "uri must be specified.")
  private String uri;
}


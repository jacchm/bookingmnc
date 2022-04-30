package com.mnc.booking.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;

@Data
public class MetadataDTO {
  private int version;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Instant createdAt;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Instant modifiedAt;
}

package com.mnc.booking.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {
  private int code;
  private String status;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Instant timestamp;
  private String message;
  private List<String> details;
}

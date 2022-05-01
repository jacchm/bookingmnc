package com.mnc.booking.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Builder
@Data
public class ErrorResponseDTO {
  private int code;
  private String status;
  private Instant timestamp;
  private String message;
  private List<String> details;
}

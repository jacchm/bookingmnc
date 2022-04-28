package com.mnc.booking.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.time.Instant;

@Data
@Embeddable
public class Metadata {
  private int version;
  private Instant createdAt;
  private Instant modifiedAt;
}

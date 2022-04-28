package com.mnc.booking.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class URI {

  @Id
  private int id;
  private String roomNo;
  private String uris;
}

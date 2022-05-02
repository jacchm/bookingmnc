package com.mnc.booking.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class SearchCriteria {

  private String key;
  private SearchOperation searchOperation;
  private boolean isOrOperation;
  private List<Object> arguments;
}

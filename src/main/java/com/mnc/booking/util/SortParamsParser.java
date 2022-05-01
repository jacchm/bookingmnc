package com.mnc.booking.util;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SortParamsParser {

  public List<Sort.Order> prepareSortOrderList(final String sortParams) {
    return List.of();
  }
}

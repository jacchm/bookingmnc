package com.mnc.booking.controller.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SortParamsParser {

  private static final String SORT_FIELDS_SEPARATOR = ",";
  private static final String SORT_EXPRESSION_SEPARATOR = ":";

  private final Properties properties;

  public List<Sort.Order> prepareSortOrderList(final String sortParams, final Class<?> clazz) {
    final List<Sort.Order> sortParamsList = new ArrayList<>();
    if (StringUtils.hasText(sortParams)) {
      Arrays.stream(sortParams.split(SORT_FIELDS_SEPARATOR))
          .forEach(sortField -> addSortParam(sortParamsList, sortField, clazz));
    }
    return sortParamsList;
  }

  private void addSortParam(final List<Sort.Order> sortParams, final String sortField, final Class<?> clazz) {
    final String[] sortExpression = sortField.split(SORT_EXPRESSION_SEPARATOR);
    final Sort.Direction sortDirection = sortExpression.length > 1 &&
        Sort.DEFAULT_DIRECTION.toString().equalsIgnoreCase(sortExpression[1]) ?
        Sort.Direction.ASC : Sort.Direction.DESC;

    if (properties.getMapOfProperties().get(clazz).contains(sortExpression[0].toLowerCase())) {
      sortParams.add(new Sort.Order(sortDirection, sortExpression[0]));
    }
  }

}

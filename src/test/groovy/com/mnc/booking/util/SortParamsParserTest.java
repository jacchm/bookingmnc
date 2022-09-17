package com.mnc.booking.util;

import com.mnc.booking.controller.util.SortParamsParser;
import com.mnc.booking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-Test class covering the sorting parameter parser functionality.
 */
class SortParamsParserTest {

  SortParamsParser sortParamsParser = new SortParamsParser();

  @BeforeEach
  void setup() {
    sortParamsParser.prepareFieldsMapForEveryEntity();
  }

  @Test
  void shouldReturnSortOrderListWithSortingParametersInProperOrderingAndDirection() {
    final List<Sort.Order> sortOrderList = sortParamsParser.prepareSortOrderList("username:asc,name", User.class);
    assertThat(sortOrderList).isNotEmpty();
    assertThat(sortOrderList).contains(new Sort.Order(Sort.Direction.ASC, "username"));
    assertThat(sortOrderList).contains(new Sort.Order(Sort.Direction.DESC, "name"));
  }

  @Test
  void shouldReturnSortOrderListWithSortingParametersInProperOrderingAndDefaultDirection() {
    final List<Sort.Order> sortOrderList = sortParamsParser.prepareSortOrderList(
        "username,email,password,name,surname,dateOfBirth,authorities,phoneNumber", User.class);
    assertThat(sortOrderList).isNotEmpty().containsExactly(
        new Sort.Order(Sort.Direction.DESC, "username"),
        new Sort.Order(Sort.Direction.DESC, "email"),
        new Sort.Order(Sort.Direction.DESC, "password"),
        new Sort.Order(Sort.Direction.DESC, "name"),
        new Sort.Order(Sort.Direction.DESC, "surname"),
        new Sort.Order(Sort.Direction.DESC, "dateOfBirth"),
        new Sort.Order(Sort.Direction.DESC, "authorities"),
        new Sort.Order(Sort.Direction.DESC, "phoneNumber"));
  }

  @Test
  void shouldReturnSortOrderListWithSortingParametersInProperOrderingAndExactDirection() {
    final List<Sort.Order> sortOrderList = sortParamsParser.prepareSortOrderList(
        "username:asc,email:ASC,password:desc,name:DESC,surname,dateOfBirth:asc,authorities,phoneNumber", User.class);
    assertThat(sortOrderList).isNotEmpty().containsExactly(
        new Sort.Order(Sort.Direction.ASC, "username"),
        new Sort.Order(Sort.Direction.ASC, "email"),
        new Sort.Order(Sort.Direction.DESC, "password"),
        new Sort.Order(Sort.Direction.DESC, "name"),
        new Sort.Order(Sort.Direction.DESC, "surname"),
        new Sort.Order(Sort.Direction.ASC, "dateOfBirth"),
        new Sort.Order(Sort.Direction.DESC, "authorities"),
        new Sort.Order(Sort.Direction.DESC, "phoneNumber"));
  }

}

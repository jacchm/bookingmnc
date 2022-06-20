package com.mnc.booking.controller.util;

import com.mnc.booking.model.Reservation;
import com.mnc.booking.model.Room;
import com.mnc.booking.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@RequiredArgsConstructor
@Component
public class SortParamsParser {

  private static final String SORT_FIELDS_SEPARATOR = ",";
  private static final String SORT_EXPRESSION_SEPARATOR = ":";

  public Map<Class<?>, List<String>> mapOfProperties;

  @PostConstruct
  public void prepareFieldsMapForEveryEntity() {
    mapOfProperties = new HashMap<>();
    mapOfProperties.put(User.class, Arrays.stream(User.class.getDeclaredFields())
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        .map(Field::getName)
        .map(String::toLowerCase)
        .toList());
    mapOfProperties.put(Room.class, Arrays.stream(Room.class.getDeclaredFields())
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        .map(Field::getName)
        .map(String::toLowerCase)
        .toList());
    mapOfProperties.put(Reservation.class, Arrays.stream(Room.class.getDeclaredFields())
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        .map(Field::getName)
        .map(String::toLowerCase)
        .toList());
  }

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

    if (mapOfProperties.get(clazz).contains(sortExpression[0].toLowerCase())) {
      sortParams.add(new Sort.Order(sortDirection, sortExpression[0]));
    }
  }

}

package com.mnc.booking.controller.util;

import com.mnc.booking.model.Room;
import com.mnc.booking.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FilterParamsParser {

  private Map<Class<?>, List<String>> mapOfProperties;

  @PostConstruct
  void prepareFieldsMapForEveryEntity() {
    mapOfProperties = new HashMap<>();
    mapOfProperties.put(User.class, Arrays.stream(User.class.getDeclaredFields())
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        .map(Field::getName)
        .map(String::toLowerCase)
        .toList());
    mapOfProperties.put(Room.class, Arrays.stream(Room.class.getDeclaredFields())
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        .map(Field::getName).map(String::toLowerCase)
        .toList());
  }

  public Map<String, String> prepareFilterParamsMap(final Map<String, String> filterParams, final Class<?> clazz) {
    if (!CollectionUtils.isEmpty(filterParams)) {
      return filterParams.entrySet().stream()
          .filter(entry -> mapOfProperties.get(clazz).contains(entry.getKey().toLowerCase()))
          .collect(Collectors.toMap(entry -> entry.getKey(), Map.Entry::getValue));
    }
    return filterParams;
  }

}

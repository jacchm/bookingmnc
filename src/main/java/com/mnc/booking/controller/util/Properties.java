package com.mnc.booking.controller.util;

import com.mnc.booking.model.Room;
import com.mnc.booking.model.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
public final class Properties {

  public Map<Class<?>, List<String>> mapOfProperties;

  @PostConstruct
  void prepareFieldsMapForEveryEntity() {
    mapOfProperties = new HashMap<>();
    mapOfProperties.put(User.class, Arrays.stream(User.class.getDeclaredFields())
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        .map(Field::getName)
        .toList());
    mapOfProperties.put(Room.class, Arrays.stream(Room.class.getDeclaredFields())
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        .map(Field::getName)
        .toList());
  }
}

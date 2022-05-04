package com.mnc.booking.controller.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class FilterParamsParser {

  private final Properties properties;

  public Map<String, String> prepareFilterParamsMap(final Map<String, String> filterParams, final Class<?> clazz) {
    if (!CollectionUtils.isEmpty(filterParams)) {
      final List<String> listOfFieldsLowercase = properties.getMapOfProperties().get(clazz)
          .stream()
          .map(String::toLowerCase)
          .collect(Collectors.toList());
      return filterParams.entrySet().stream()
          .filter(entry -> listOfFieldsLowercase.contains(entry.getKey().toLowerCase()))
          .map(entry -> establishExactFieldName(entry, clazz))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    return filterParams;
  }


  private Map.Entry<String, String> establishExactFieldName(final Map.Entry<String, String> entry, final Class<?> clazz) {
    final List<String> fields = properties.getMapOfProperties().get(clazz);
    String exactFieldName = entry.getKey();
    for (String field : fields) {
      if (field.equalsIgnoreCase(entry.getKey())) {
        exactFieldName = field;
        break;
      }
    }
    return Map.entry(exactFieldName, entry.getValue());
  }

}

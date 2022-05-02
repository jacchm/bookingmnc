package com.mnc.booking.util;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SpecificationFactory<T> {

  public Specification<T> isEqual(final String key, final Object arg) {
    final GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
    return builder.with(key, SearchOperation.EQUALITY, Collections.singletonList(arg)).build();
  }

  public Specification<T> isNotEqual(final String key, final Object arg) {
    final GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
    return builder.with(key, SearchOperation.NOT_EQUAL, Collections.singletonList(arg)).build();
  }

  public Specification<T> isLike(final String key, final Object arg) {
    final GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
    return builder.with(key, SearchOperation.LIKE, Collections.singletonList(arg)).build();
  }

  public Specification<T> isGreaterThan(final String key, final Comparable<?> arg) {
    final GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
    return builder.with(key, SearchOperation.GREATER_THAN, Collections.singletonList(arg)).build();
  }

  public Specification<T> isGreaterThanOrEqualTo(final String key, final Comparable<?> arg) {
    final GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
    return builder.with(key, SearchOperation.GREATER_THAN_OR_EQUAL_TO, Collections.singletonList(arg)).build();
  }

  public Specification<T> isLessThan(final String key, final Comparable<?> arg) {
    final GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
    return builder.with(key, SearchOperation.LESS_THAN, Collections.singletonList(arg)).build();
  }

  public Specification<T> isLessThanOrEqualTo(final String key, final Comparable<?> arg) {
    final GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
    return builder.with(key, SearchOperation.LESS_THAN_OR_EQUAL_TO, Collections.singletonList(arg)).build();
  }

  public Specification<T> isIn(final String key, final List<Object> arg) {
    final GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
    return builder.with(key, SearchOperation.IN, Collections.singletonList(arg)).build();
  }
}

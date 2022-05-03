package com.mnc.booking.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class GenericSpecification<T> implements Specification<T> {

  private SearchCriteria searchCriteria;

  @Override
  public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
    final List<Object> arguments = searchCriteria.getArguments();
    final Object arg = arguments.get(0);

    return switch (searchCriteria.getSearchOperation()) {
      case EQUALITY -> criteriaBuilder.equal(root.get(searchCriteria.getKey()), arg);
      case NOT_EQUAL -> criteriaBuilder.notEqual(root.get(searchCriteria.getKey()), arg);
      case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(searchCriteria.getKey()), (Comparable) arg);
      case GREATER_THAN_OR_EQUAL_TO -> criteriaBuilder.greaterThanOrEqualTo(root.get(searchCriteria.getKey()), (Comparable) arg);
      case LESS_THAN -> criteriaBuilder.lessThan(root.get(searchCriteria.getKey()), (Comparable) arg);
      case LESS_THAN_OR_EQUAL_TO -> criteriaBuilder.lessThanOrEqualTo(root.get(searchCriteria.getKey()), (Comparable) arg);
      case LIKE -> criteriaBuilder.like(root.get(searchCriteria.getKey()), (String) arg);
      case IN -> root.get(searchCriteria.getKey()).in(arguments);
    };
  }
}

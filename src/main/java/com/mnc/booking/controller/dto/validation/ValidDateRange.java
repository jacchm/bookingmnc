package com.mnc.booking.controller.dto.validation;


import com.mnc.booking.controller.dto.reservation.DateRangeDTO;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.Instant;
import java.util.Objects;

@Documented
@Constraint(validatedBy = ValidDateRange.DateRangeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {

  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRangeDTO> {

    @Override
    public boolean isValid(final DateRangeDTO value, final ConstraintValidatorContext context) {
      context.disableDefaultConstraintViolation();
      if (!allValuesArePresent(value)) {
        context
            .buildConstraintViolationWithTemplate("dateRange.from and dateRange.to must be specified.")
            .addConstraintViolation();
        return false;
      }
      if (!allValuesAreLaterThanNow(value)) {
        context
            .buildConstraintViolationWithTemplate("dateRange.from and dateRange.to must be after now.")
            .addConstraintViolation();
        return false;
      }
      if (!value.getFrom().isBefore(value.getTo())) {
        context
            .buildConstraintViolationWithTemplate("dateRange.from must be before dateRange.to.")
            .addConstraintViolation();
        return false;
      }
      return true;
    }

    private boolean allValuesArePresent(final DateRangeDTO value) {
      return Objects.nonNull(value) && Objects.nonNull(value.getFrom()) && Objects.nonNull(value.getTo());
    }

    private boolean allValuesAreLaterThanNow(final DateRangeDTO value) {
      return value.getFrom().isAfter(Instant.now()) && value.getTo().isAfter(Instant.now());
    }

  }

}

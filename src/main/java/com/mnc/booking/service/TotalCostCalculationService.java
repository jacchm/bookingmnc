package com.mnc.booking.service;

import com.mnc.booking.model.Reservation;
import com.mnc.booking.model.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class TotalCostCalculationService {

  private static final BigDecimal BREAKFAST_COST_PER_PERSON_PER_DAY = BigDecimal.valueOf(15L); // 15EUR
  private static final BigDecimal DINNER_COST_PER_PERSON_PER_DAY = BigDecimal.valueOf(15L); // 15EUR
  private static final BigDecimal SUPPER_COST_PER_PERSON_PER_DAY = BigDecimal.valueOf(15L); // 15EUR
  private static final BigDecimal ANIMALS_COST_PER_DAY = BigDecimal.valueOf(10L); // 10EUR
  private static final BigDecimal PARKING_COST_PER_DAY = BigDecimal.valueOf(10L); // 10EUR

  public BigDecimal calculateReservationCosts(final Reservation reservation, final Room room) {
    BigDecimal totalCost = BigDecimal.ZERO;
    final long numberOfDays = ChronoUnit.DAYS.between(reservation.getDateFrom(), reservation.getDateTo()) + 1;
    log.info("number of days between specified instants = {}", numberOfDays);
    log.info("number of people = {}", reservation.getNoPeople());
    totalCost = addCostsIfIncludedInReservation(totalCost, reservation.isBreakfastIncluded(), BREAKFAST_COST_PER_PERSON_PER_DAY);
    totalCost = addCostsIfIncludedInReservation(totalCost, reservation.isDinnerIncluded(), DINNER_COST_PER_PERSON_PER_DAY);
    totalCost = addCostsIfIncludedInReservation(totalCost, reservation.isSupperIncluded(), SUPPER_COST_PER_PERSON_PER_DAY);
    log.info("totalCost per day per person before parking and animal costs added = {}", totalCost.toString());
    totalCost = totalCost.multiply(BigDecimal.valueOf(reservation.getNoPeople()));
    log.info("totalCost per day for all family before room , parking and animal costs added = {}", totalCost);
    totalCost = addCostsIfIncludedInReservation(totalCost, reservation.isAnimalsIncluded(), ANIMALS_COST_PER_DAY);
    totalCost = addCostsIfIncludedInReservation(totalCost, reservation.isParkingIncluded(), PARKING_COST_PER_DAY);
    totalCost = totalCost.add(room.getPricePerNightValue());
    log.info("totalCost per day after room, parking and animal costs added = {}", totalCost);
    totalCost = totalCost.multiply(BigDecimal.valueOf(numberOfDays));
    log.info("totalCost = {}", totalCost);

    return totalCost;
  }

  private BigDecimal addCostsIfIncludedInReservation(BigDecimal totalCost, final boolean animalsIncluded, final BigDecimal animalsCost) {
    if (animalsIncluded) {
      totalCost = totalCost.add(animalsCost);
    }
    return totalCost;
  }
}

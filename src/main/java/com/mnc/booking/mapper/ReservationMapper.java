package com.mnc.booking.mapper;

import com.mnc.booking.controller.dto.reservation.ReservationCreationDTO;
import com.mnc.booking.controller.dto.reservation.ReservationDTO;
import com.mnc.booking.model.Reservation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    uses = {PriceMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReservationMapper {

  @Mapping(source = "dateRange.from", target = "dateFrom")
  @Mapping(source = "dateRange.to", target = "dateTo")
  @Mapping(target = "status", constant = "PENDING")
  @Mapping(target = "totalCostCurrency", constant = "EUR")
  Reservation mapToReservation(final ReservationCreationDTO reservationCreationDTO);

  @Mapping(source = "dateFrom", target = "dateRange.from")
  @Mapping(source = "dateTo", target = "dateRange.to")
  @Mapping(source = "totalCostValue", target = "totalCost.value")
  @Mapping(source = "totalCostCurrency", target = "totalCost.currency")
  ReservationDTO mapToReservationDTO(final Reservation reservation);

}

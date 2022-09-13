package com.mnc.booking.service;

import com.mnc.booking.controller.dto.reservation.DateRangeDTO;
import com.mnc.booking.controller.dto.reservation.PaymentDTO;
import com.mnc.booking.controller.dto.reservation.ReservationFilterParams;
import com.mnc.booking.controller.util.SortParamsParser;
import com.mnc.booking.exception.BadRequestException;
import com.mnc.booking.exception.NotFoundException;
import com.mnc.booking.model.Reservation;
import com.mnc.booking.model.ReservationStatus;
import com.mnc.booking.model.Room;
import com.mnc.booking.repository.ReservationRepository;
import com.mnc.booking.repository.RoomRepository;
import com.mnc.booking.repository.UserRepository;
import com.mnc.booking.util.GenericSpecificationsBuilder;
import com.mnc.booking.util.SpecificationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mnc.booking.model.ReservationStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

  private static final String RESERVATION_NOT_FOUND_ERROR_MSG = "Reservation with id=%s has not been found.";
  private static final String RESERVATION_FOR_USER_NOT_FOUND_ERROR_MSG = "Reservation with id=%s for user with username=%s has not been found.";
  private static final String RESERVATION_STATUS_UPDATE_NOT_ALLOWED_ERROR_MSG = "Reservation with id=%s and current status=%s cannot be transitioned to status=%s. Please check the available transitions.";
  private static final String USER_NOT_FOUND_ERROR_MSG = "User with username=%s has not been found. Reservation cannot be proceeded.";
  private static final String ROOM_NOT_FOUND_ERROR_MSG = "Room with roomNo=%s has not been found. Reservation cannot be proceeded.";
  private static final String ROOM_CAPACITY_EXCEEDED_ERROR_MSG = "Room with roomNo=%s has smaller capacity than requested. Reservation cannot be proceeded. Please find another room.";
  private static final String ROOM_NOT_AVAILABLE_FOUND_ERROR_MSG = "Room with roomNo=%s is not available for a given time slot. Reservation cannot be proceeded. Please find an available room.";
  private static final String PAYMENT_WRONG_CURRENCY_ERROR_MSG = "Payment with currency %s cannot be performed. Currency should be equal to %s.";
  private static final String PAYMENT_WRONG_AMOUNT_ERROR_MSG = "Payment with amount %s cannot be performed. Amount should be equal to %s.";

  private final SortParamsParser sortParamsParser;
  private final SpecificationFactory<Reservation> reservationSpecificationFactory;
  private final TotalCostCalculationService totalCostCalculationService;
  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;

  public Long createReservation(final Reservation reservation) {
    throwErrorIfUserWithGivenUsernameDoesNotExist(reservation.getUsername());
    final String requestedRoomNo = reservation.getRoomNo();
    final Room requestedRoom = fetchRoom(requestedRoomNo);

    throwAnErrorIfRoomHasSmallerCapacityThanGivenNumberOfPeople(
        reservation.getNoPeople(), requestedRoom.getNoPeople(), requestedRoomNo);
    throwErrorIfRoomIsNotAvailableForGivenDateRange(
        requestedRoomNo, reservation.getDateFrom(), reservation.getDateTo());

    final BigDecimal totalCost = totalCostCalculationService.calculateReservationCosts(reservation, requestedRoom);
    reservation.setTotalCostValue(totalCost);

    return reservationRepository.save(reservation).getId();
  }

  public Page<Reservation> getReservations(final Integer pageNumber, final Integer pageSize, final String sortParams,
                                           final ReservationFilterParams filterParams) {
    final Sort sort = Sort.by(sortParamsParser.prepareSortOrderList(sortParams, Room.class));
    final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

    return reservationRepository.findAll(buildFilteringQuery(filterParams), pageable);
  }

  public Reservation getReservation(final Long reservationId) {
    return reservationRepository.findById(reservationId)
        .orElseThrow(() -> new NotFoundException(String.format(RESERVATION_NOT_FOUND_ERROR_MSG, reservationId)));
  }

  public void performPayment(final Long reservationId, final PaymentDTO paymentDTO) {
    final Reservation reservation = reservationRepository.findByIdAndUsername(reservationId, paymentDTO.getUsername())
        .orElseThrow(() -> new NotFoundException(String.format(
            RESERVATION_FOR_USER_NOT_FOUND_ERROR_MSG, reservationId, paymentDTO.getUsername())));
    // TODO: here is the place to implement payment via payment service.
    //  Payment service should be integrated with real payment platform.

    if (!paymentDTO.getPayment().getValue().equals(reservation.getTotalCostValue())) {
      throw new BadRequestException(
          String.format(PAYMENT_WRONG_AMOUNT_ERROR_MSG, paymentDTO.getPayment().getValue(), reservation.getTotalCostValue()));
    }
    if (!paymentDTO.getPayment().getCurrency().equals(reservation.getTotalCostCurrency())) {
      throw new BadRequestException(
          String.format(PAYMENT_WRONG_CURRENCY_ERROR_MSG, paymentDTO.getPayment().getCurrency(), reservation.getTotalCostCurrency()));
    }
    reservation.setStatus(ReservationStatus.PAID);
    reservationRepository.save(reservation);
  }

  public void updateReservationStatus(final Long reservationId, final ReservationStatus newReservationStatus) {
    final Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new NotFoundException(String.format(RESERVATION_NOT_FOUND_ERROR_MSG, reservationId)));
    final ReservationStatus currentStatus = reservation.getStatus();

    if ((PENDING.equals(currentStatus) && !PENDING.equals(newReservationStatus) && !ACCEPTED.equals(newReservationStatus)) ||
        (PAID.equals(currentStatus) && !PAID.equals(newReservationStatus) && !PENDING.equals(newReservationStatus)) ||
        (REJECTED.equals(currentStatus)) ||
        (ACCEPTED.equals(currentStatus))) {
      reservationRepository.setReservationStatus(reservationId, newReservationStatus);
      return;
    }
    throw new BadRequestException(
        String.format(RESERVATION_STATUS_UPDATE_NOT_ALLOWED_ERROR_MSG, reservationId, currentStatus, newReservationStatus));
  }

  private Specification<Reservation> buildFilteringQuery(final ReservationFilterParams filters) {
    final GenericSpecificationsBuilder<Reservation> builder = new GenericSpecificationsBuilder<>();
    if (Objects.nonNull(filters)) {
      ReflectionUtils.doWithFields(ReservationFilterParams.class,
          field -> {
            field.setAccessible(true);
            if (Objects.nonNull(field.get(filters))) {
              if (field.getName().equals("minTotalCostValue")) {
                builder.with(reservationSpecificationFactory.isGreaterThanOrEqualTo(
                    "totalCostValue", (BigDecimal) field.get(filters), builder));
              } else if (field.getName().equals("maxTotalCostValue")) {
                builder.with(reservationSpecificationFactory.isLessThanOrEqualTo(
                    "totalCostValue", (BigDecimal) field.get(filters), builder));
              } else if (field.getName().equals("activeDate")) {
                builder.with(reservationSpecificationFactory.isLessThanOrEqualTo(
                    "dateFrom", (Instant) field.get(filters), builder));
                builder.with(reservationSpecificationFactory.isGreaterThanOrEqualTo(
                    "dateTo", (Instant) field.get(filters), builder));
              } else {
                builder.with(reservationSpecificationFactory.isEqual(field.getName(), field.get(filters), builder));
              }
            }
          });
    }
    return builder.build();
  }

  private void throwAnErrorIfRoomHasSmallerCapacityThanGivenNumberOfPeople(final int noPeople, final int roomCapacity,
                                                                           final String roomNo) {
    if (noPeople > roomCapacity) {
      throw new BadRequestException(String.format(ROOM_CAPACITY_EXCEEDED_ERROR_MSG, roomNo));
    }
  }

  private void throwErrorIfUserWithGivenUsernameDoesNotExist(final String username) {
    if (!userRepository.existsById(username)) {
      throw new BadRequestException(String.format(USER_NOT_FOUND_ERROR_MSG, username));
    }
  }

  private Room fetchRoom(final String roomNo) {
    return roomRepository.findById(roomNo)
        .orElseThrow(() -> new BadRequestException(String.format(ROOM_NOT_FOUND_ERROR_MSG, roomNo)));
  }

  private void throwErrorIfRoomIsNotAvailableForGivenDateRange(final String requestedRoomNo, final Instant dateFrom,
                                                               final Instant dateTo) {
    if (reservationRepository.findReservationByDateRange(dateFrom, dateTo, requestedRoomNo) != 0) {
      throw new BadRequestException(String.format(ROOM_NOT_AVAILABLE_FOUND_ERROR_MSG, requestedRoomNo));
    }
  }

  public List<DateRangeDTO> getUnavailabilityForRoom(final String roomNo) {
    final Optional<List<Reservation>> reservationsForRoom = reservationRepository.findAllPaidAndAcceptedReservationsForRoom(roomNo, Instant.now());
    return reservationsForRoom.map(reservations ->
            reservations
                .stream()
                .map(reservation -> DateRangeDTO.of(reservation.getDateFrom(), reservation.getDateTo()))
                .collect(Collectors.toList()))
        .orElse(List.of());
  }

}

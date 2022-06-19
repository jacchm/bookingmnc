package com.mnc.booking.service;

import com.mnc.booking.controller.util.SortParamsParser;
import com.mnc.booking.exception.BadRequestException;
import com.mnc.booking.model.Reservation;
import com.mnc.booking.model.Room;
import com.mnc.booking.repository.ReservationRepository;
import com.mnc.booking.repository.RoomRepository;
import com.mnc.booking.repository.UserRepository;
import com.mnc.booking.util.SpecificationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

  private static final String RESERVATION_NOT_FOUND_ERROR_MSG = "Reservation with id=%s has not been found.";
  private static final String USER_NOT_FOUND_ERROR_MSG = "User with username=%s has not been found. Reservation cannot be proceeded.";
  private static final String ROOM_NOT_FOUND_ERROR_MSG = "Room with roomNo=%s has not been found. Reservation cannot be proceeded.";
  private static final String ROOM_CAPACITY_EXCEEDED_ERROR_MSG = "Room with roomNo=%s has smaller capacity than requested. Reservation cannot be proceeded. Please find another room.";
  private static final String ROOM_NOT_AVAILABLE_FOUND_ERROR_MSG = "Room with roomNo=%s is not available for a given time slot. Reservation cannot be proceeded. Please find an available room.";

  private final SortParamsParser sortParamsParser;
  private final SpecificationFactory<Room> roomSpecificationFactory;
  private final CostCalculationsService costCalculationsService;
  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;

  public Long createReservation(final Reservation reservation) {
    throwErrorIfUserWithGivenUsernameDoesNotExist(reservation.getUsername());
    final String requestedRoomNo = reservation.getRoomNo();
    final Room requestedRoom = fetchRoom(requestedRoomNo);

    throwAnErrorIfRoomHasSmallerCapacityThanGivenNumberOfPeople(
        reservation.getNoPeople(), requestedRoom.getNoPeople(), requestedRoomNo);
    throwErrorIfRommIsNotAvailableForGivenDateRange(
        requestedRoomNo, reservation.getDateFrom(), reservation.getDateTo());

    final BigDecimal totalCost = costCalculationsService.calculateReservationCosts(reservation, requestedRoom);
    reservation.setTotalCostValue(totalCost);

    return reservationRepository.save(reservation).getId();
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

  // TODO: implement time range
  private boolean throwErrorIfRommIsNotAvailableForGivenDateRange(final String requestedRoomNo, final Instant dateFrom,
                                                                  final Instant dateTo) {
    if (false) {
      throw new BadRequestException(String.format(ROOM_NOT_AVAILABLE_FOUND_ERROR_MSG, requestedRoomNo));
    }
    return true;
  }

}

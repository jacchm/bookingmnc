package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.reservation.ReservationCreationDTO;
import com.mnc.booking.controller.dto.reservation.ReservationCreationResponseDTO;
import com.mnc.booking.mapper.ReservationMapper;
import com.mnc.booking.model.Reservation;
import com.mnc.booking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("reservations")
@RestController
public class ReservationController {

  private static final String X_TOTAL_COUNT = "X-TOTAL-COUNT";
  private static final String FIELD_DIR_REGEX = "[-\\w]+(\\.[-\\w]+)*(:[a-zA-Z]+)?";
  private static final String SORT_REGEX = "^" + FIELD_DIR_REGEX + "(," + FIELD_DIR_REGEX + ")*$";

  private final ReservationService reservationService;
  private final ReservationMapper reservationMapper;

  @PostMapping
  public ResponseEntity<ReservationCreationResponseDTO> createRoom(@Valid @RequestBody final ReservationCreationDTO reservationCreationDTO) {
    log.info("Reservation creation request received with reservationCreationDto={}", reservationCreationDTO);
    final Reservation newReservation = reservationMapper.mapToReservation(reservationCreationDTO);
    final Long reservationId = reservationService.createReservation(newReservation);
    return new ResponseEntity<>(ReservationCreationResponseDTO.of(reservationId), HttpStatus.CREATED);
  }

}

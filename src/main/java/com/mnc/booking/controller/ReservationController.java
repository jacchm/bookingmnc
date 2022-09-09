package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.reservation.*;
import com.mnc.booking.mapper.ReservationMapper;
import com.mnc.booking.model.Reservation;
import com.mnc.booking.model.ReservationStatus;
import com.mnc.booking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

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
  public ResponseEntity<ReservationCreationResponseDTO> createReservation(@Valid @RequestBody final ReservationCreationDTO reservationCreationDTO) {
    log.info("Reservation creation request received with reservationCreationDto={}", reservationCreationDTO);
    final Reservation newReservation = reservationMapper.mapToReservation(reservationCreationDTO);
    final Long reservationId = reservationService.createReservation(newReservation);
    return new ResponseEntity<>(ReservationCreationResponseDTO.of(reservationId), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<ReservationDTO>> getReservations(@RequestHeader(name = X_TOTAL_COUNT, required = false, defaultValue = "false") final boolean xTotalCount,
                                                              @RequestParam(required = false, defaultValue = "1") final Integer pageNumber,
                                                              @RequestParam(required = false, defaultValue = "10") final Integer pageSize,
                                                              @Pattern(regexp = SORT_REGEX, message = "Sort parameters should be in form of field:direction (ASC/DESC) separated by ',' (comma).")
                                                              @RequestParam(required = false, defaultValue = "") final String sort,
                                                              @Valid final ReservationFilterParams filterParams) {
    log.info("Rooms fetch request received with params: pageSize={}, pageNumber={}, xTotalCount={}, sortParams={} and filterParams={}", pageSize, pageNumber, xTotalCount, sort, filterParams);
    final Page<Reservation> result = reservationService.getReservations(pageNumber, pageSize, sort, filterParams);
    final List<ReservationDTO> reservations = result.getContent()
        .stream()
        .map(reservationMapper::mapToReservationDTO)
        .collect(Collectors.toList());
    final HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add(X_TOTAL_COUNT, xTotalCount ? String.valueOf(result.getTotalElements()) : null);

    return ResponseEntity.ok().headers(responseHeaders).body(reservations);
  }

  @GetMapping("/{reservationId}")
  public ResponseEntity<ReservationDTO> getReservation(@PathVariable @Min(value = 1, message = "Provide a valid reservationId") final Long reservationId) {
    log.info("Reservation fetch request received for reservationId={}", reservationId);

    final ReservationDTO reservationDTO = reservationMapper.mapToReservationDTO(reservationService.getReservation(reservationId));
    return ResponseEntity.ok(reservationDTO);
  }

  @PostMapping("/{reservationId}/payment")
  public ResponseEntity<ReservationDTO> payForReservation(@PathVariable @Min(value = 1, message = "Provide a valid reservationId") final Long reservationId,
                                                          @Valid @RequestBody final PaymentDTO paymentDTO) {
    log.info("Pay for reservation request received for reservationId={}", reservationId);

    reservationService.performPayment(reservationId, paymentDTO);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{reservationId}")
  public ResponseEntity<ReservationDTO> updateReservationStatus(@PathVariable @Min(value = 1, message = "Provide a valid reservationId") final Long reservationId,
                                                                @Valid @RequestBody final ReservationStatusUpdateDTO reservationStatusUpdateDTO) {
    log.info("Update reservation's status to={} received for reservationId={}", reservationStatusUpdateDTO.getStatus(), reservationId);
    final ReservationStatus newReservationStatus = ReservationStatus.valueOf(reservationStatusUpdateDTO.getStatus());
    reservationService.updateReservationStatus(reservationId, newReservationStatus);
    return ResponseEntity.ok().build();
  }

}

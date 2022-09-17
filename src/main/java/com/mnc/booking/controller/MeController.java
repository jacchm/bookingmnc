package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.reservation.*;
import com.mnc.booking.controller.dto.user.UserDTO;
import com.mnc.booking.controller.dto.user.UserPasswordUpdateDTO;
import com.mnc.booking.controller.dto.user.UserUpdateDTO;
import com.mnc.booking.exception.NotFoundException;
import com.mnc.booking.mapper.ReservationMapper;
import com.mnc.booking.mapper.UserMapper;
import com.mnc.booking.model.Reservation;
import com.mnc.booking.service.ReservationService;
import com.mnc.booking.service.UserService;
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
@RestController
@RequestMapping("/me")
public class MeController {

  private static final String X_TOTAL_COUNT = "X-TOTAL-COUNT";
  private static final String FIELD_DIR_REGEX = "[-\\w]+(\\.[-\\w]+)*(:[a-zA-Z]+)?";
  private static final String SORT_REGEX = "^" + FIELD_DIR_REGEX + "(," + FIELD_DIR_REGEX + ")*$|^$";

  private static final String USER_NOT_FOUND_ERROR_MSG = "User with username=%s has not been found.";

  private final UserService userService;
  private final UserMapper userMapper;
  private final ReservationService reservationService;
  private final ReservationMapper reservationMapper;

  @GetMapping
  public ResponseEntity<UserDTO> getMe(@RequestHeader final String username) {
    log.info("User with username={} get request.", username);
    final UserDTO userDTO = userService.getUser(username)
        .map(userMapper::mapToUserDTO)
        .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_ERROR_MSG, username)));
    return ResponseEntity.ok(userDTO);
  }

  @PutMapping
  public ResponseEntity<Void> updateMe(@RequestHeader final String username,
                                       @Valid @RequestBody final UserUpdateDTO userUpdateDTO) {
    log.info("User with username={} update request with update={}", username, userUpdateDTO);
    userService.updateUser(username, userUpdateDTO);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping
  public ResponseEntity<Void> resetPassword(@RequestHeader final String username,
                                            @Valid @RequestBody final UserPasswordUpdateDTO userPasswordUpdateDTO) {
    log.info("Resetting password for user with username={}", username);
    userService.updatePassword(username, userPasswordUpdateDTO.getPassword(), userPasswordUpdateDTO.getNewPassword());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteMe(@RequestHeader final String username) {
    userService.deleteUser(username);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/reservations")
  public ResponseEntity<ReservationCreationResponseDTO> createReservation(@RequestHeader final String username,
                                                                          @Valid @RequestBody final ReservationCreationDTO reservationCreationDTO) {
    log.info("Reservation creation request received with reservationCreationDto={}", reservationCreationDTO);
    reservationCreationDTO.setUsername(username);
    final Reservation newReservation = reservationMapper.mapToReservation(reservationCreationDTO);
    final Long reservationId = reservationService.createReservation(newReservation);
    return new ResponseEntity<>(ReservationCreationResponseDTO.of(reservationId), HttpStatus.CREATED);
  }

  @GetMapping("/reservations")
  public ResponseEntity<List<ReservationDTO>> getMyReservations(@RequestHeader(name = X_TOTAL_COUNT, required = false, defaultValue = "false") final boolean xTotalCount,
                                                                @RequestParam(required = false, defaultValue = "1") final Integer pageNumber,
                                                                @RequestParam(required = false, defaultValue = "10") final Integer pageSize,
                                                                @Pattern(regexp = SORT_REGEX, message = "Sort parameters should be in form of field:direction (ASC/DESC) separated by ',' (comma).")
                                                                @RequestParam(required = false, defaultValue = "") final String sort,
                                                                @Valid final ReservationFilterParams filterParams,
                                                                @RequestHeader final String username) {
    log.info("User with username={} get request.", username);
    filterParams.setUsername(username);
    final Page<Reservation> result = reservationService.getReservations(pageNumber, pageSize, sort, filterParams);
    final List<ReservationDTO> reservations = result.getContent()
        .stream()
        .map(reservationMapper::mapToReservationDTO)
        .collect(Collectors.toList());
    final HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add(X_TOTAL_COUNT, xTotalCount ? String.valueOf(result.getTotalElements()) : null);

    return ResponseEntity.ok().headers(responseHeaders).body(reservations);
  }

  @PostMapping("/reservations/{reservationId}/payment")
  public ResponseEntity<ReservationDTO> payForReservation(@PathVariable @Min(value = 1, message = "Provide a valid reservationId") final Long reservationId,
                                                          @Valid @RequestBody final PaymentDTO paymentDTO,
                                                          @RequestHeader final String username) {
    log.info("Pay for reservation request received for reservationId={} and username={}", reservationId, username);
    paymentDTO.setUsername(username);

    reservationService.performPayment(reservationId, paymentDTO);
    return ResponseEntity.ok().build();
  }

}

package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.reservation.DateRangeDTO;
import com.mnc.booking.controller.dto.room.*;
import com.mnc.booking.controller.dto.validation.URICreation;
import com.mnc.booking.mapper.RoomMapper;
import com.mnc.booking.model.Room;
import com.mnc.booking.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("rooms")
@RestController
public class RoomController {

  private static final String X_TOTAL_COUNT = "X-TOTAL-COUNT";
  private static final String FIELD_DIR_REGEX = "[-\\w]+(\\.[-\\w]+)*(:[a-zA-Z]+)?";
  private static final String SORT_REGEX = "^" + FIELD_DIR_REGEX + "(," + FIELD_DIR_REGEX + ")*$|^$";
  private static final String FULL_SORT_REGEX = SORT_REGEX + "|^$";

  private final RoomService roomService;
  private final RoomMapper roomMapper;

  @PostMapping
  public ResponseEntity<RoomCreateResponseDTO> createRoom(@Validated(value = {Default.class}) @RequestBody final RoomCreationDTO roomCreationDTO) {
    log.info("Room creation request received with roomCreationDto={}", roomCreationDTO);
    final String roomNo = roomService.createRoom(roomCreationDTO);
    return new ResponseEntity<>(RoomCreateResponseDTO.of(roomNo), HttpStatus.CREATED);
  }

  @GetMapping({"{roomNo}"})
  public ResponseEntity<RoomDTO> getRoom(@PathVariable @NotBlank(message = "Provide a valid roomNo.") final String roomNo) {
    log.info("Room fetch request received with roomNo={}", roomNo);
    final RoomDTO roomDTO = roomService.getRoom(roomNo);
    return ResponseEntity.ok(roomDTO);
  }

  @GetMapping
  public ResponseEntity<List<RoomDTO>> getRooms(@RequestHeader(name = X_TOTAL_COUNT, required = false, defaultValue = "false") final boolean xTotalCount,
                                                @RequestParam(required = false, defaultValue = "1") final Integer pageNumber,
                                                @RequestParam(required = false, defaultValue = "10") final Integer pageSize,
                                                @Pattern(regexp = FULL_SORT_REGEX, message = "Sort parameters should be in form of field:direction (ASC/DESC) separated by ',' (comma).")
                                                @RequestParam(required = false, defaultValue = "") final String sort,
                                                @Valid final RoomFilterParams filterParams) {
    log.info("Rooms fetch request received with params: pageSize={}, pageNumber={}, xTotalCount={}, sortParams={} and filterParams={}", pageSize, pageNumber, xTotalCount, sort, filterParams);
    final Page<Room> result = roomService.getRooms(pageNumber, pageSize, sort, filterParams);
    final List<RoomDTO> rooms = result.getContent().stream().map(roomMapper::mapToRoomDTO).collect(Collectors.toList());
    final HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add(X_TOTAL_COUNT, xTotalCount ? String.valueOf(result.getTotalElements()) : null);

    return ResponseEntity.ok().headers(responseHeaders).body(rooms);
  }

  @GetMapping("/search")
  public ResponseEntity<List<RoomDTO>> findAvailableRooms(@RequestHeader(name = X_TOTAL_COUNT, required = false, defaultValue = "false") final boolean xTotalCount,
                                                          @RequestParam(required = false, defaultValue = "1") final Integer pageNumber,
                                                          @RequestParam(required = false, defaultValue = "10") final Integer pageSize,
                                                          @Nullable @Pattern(regexp = FULL_SORT_REGEX, message = "Sort parameters should be in form of field:direction (ASC/DESC) separated by ',' (comma).")
                                                          @RequestParam(required = false, defaultValue = "") final String sort,
                                                          @Valid final RoomSearchParams filterParams) {
    log.info("Rooms search request received with params: pageSize={}, pageNumber={}, xTotalCount={}, sortParams={} and filterParams={}", pageSize, pageNumber, xTotalCount, sort, filterParams);
    String finalSort = sort;
    if (filterParams.getNoPeople() != null) {
      finalSort = "noPeople:ASC";
    }
    final Page<Room> result = roomService.findAvailableRooms(pageNumber, pageSize, finalSort, filterParams);
    final List<RoomDTO> rooms = result.getContent().stream().map(roomMapper::mapToRoomDTO).collect(Collectors.toList());
    final HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add(X_TOTAL_COUNT, xTotalCount ? String.valueOf(result.getTotalElements()) : null);

    return ResponseEntity.ok().headers(responseHeaders).body(rooms);
  }

  @PutMapping({"{roomNo}"})
  public ResponseEntity<Void> updateRoom(@PathVariable final String roomNo,
                                         @RequestBody @Valid final RoomUpdateDTO roomUpdateDto) {
    log.info("Room update request received with roomNo={}, and body={}", roomNo, roomUpdateDto);
    final Room roomUpdate = roomMapper.mapToRoom(roomUpdateDto);
    roomService.updateRoom(roomNo, roomUpdate);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping({"{roomNo}"})
  public ResponseEntity<Void> partialUpdateRoom(@PathVariable final String roomNo,
                                                @RequestBody @Valid final RoomUpdateDTO roomUpdateDto) {
    log.info("Room partial update request received with roomNo={}, and body={}", roomNo, roomUpdateDto);
    final Room roomUpdate = roomMapper.mapToRoom(roomUpdateDto);
    roomService.partialUpdateRoom(roomNo, roomUpdate);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping({"{roomNo}"})
  public ResponseEntity<Void> deleteRoom(@PathVariable final String roomNo) {
    log.info("Room deletion request received with roomNo={}", roomNo);
    roomService.deleteRoom(roomNo);
    return ResponseEntity.noContent().build();
  }

  @GetMapping({"{roomNo}/unavailability"})
  public ResponseEntity<List<DateRangeDTO>> getRoomUnavailability(@PathVariable @NotBlank(message = "Provide a valid roomNo.") final String roomNo) {
    log.info("Room unavailability fetch request received with roomNo={}", roomNo);
    final List<DateRangeDTO> dateRangeDTOs = roomService.getRoomUnavailabilityDateRanges(roomNo);
    return ResponseEntity.ok(dateRangeDTOs);
  }

  @PostMapping("{roomNo}/uris")
  public ResponseEntity<URICreateResponseDTO> addUri(@PathVariable final String roomNo,
                                                     @Validated(value = {Default.class, URICreation.class}) @RequestBody final URIDTO uriDto) {
    log.info("URI creation request received for roomNo={} with uriDto={}", roomNo, uriDto);
    final Integer uriId = roomService.addUri(uriDto);
    return new ResponseEntity<>(URICreateResponseDTO.of(uriId), HttpStatus.CREATED);
  }

  @DeleteMapping("{roomNo}/uris/{uriId}")
  public ResponseEntity<Void> deleteUri(@PathVariable final String roomNo,
                                        @PathVariable @Min(value = 1, message = "Please provide valid uriId.") final Integer uriId) {
    log.info("URI deletion request received for roomNo={} with uriDto={}", roomNo, uriId);
    roomService.deleteUri(uriId, roomNo);
    return ResponseEntity.noContent().build();
  }

  @GetMapping({"{roomNo}/uris"})
  public ResponseEntity<List<URIDTO>> getUrisForRoomNo(@PathVariable final String roomNo) {
    log.info("URI fetch request received for roomNo={}", roomNo);
    return ResponseEntity.ok().build();
  }

}

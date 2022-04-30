package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.RoomCreateResponseDTO;
import com.mnc.booking.controller.dto.RoomCreationDTO;
import com.mnc.booking.controller.dto.RoomUpdateDTO;
import com.mnc.booking.model.Room;
import com.mnc.booking.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("rooms")
@RestController
public class RoomController {

  private final RoomService roomService;

  @PostMapping
  public ResponseEntity<RoomCreateResponseDTO> createRoom(@Valid @RequestBody final RoomCreationDTO roomCreationDTO) {
    log.info("Room creation request received with roomCreationDto={}", roomCreationDTO);
    final String roomNo = roomService.createRoom(roomCreationDTO);
    return new ResponseEntity<>(RoomCreateResponseDTO.of(roomNo), HttpStatus.CREATED);
  }

  @GetMapping({"{roomNo}"})
  public ResponseEntity<Room> getRoom(@PathVariable final String roomNo) {
    log.info("Room fetch request received with roomNo={}", roomNo);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<Room>> getRooms() {
    log.info("Rooms fetch request received with params="); // TODO: implement filter and sorting params
    return ResponseEntity.ok(List.of());
  }

  @PutMapping({"{roomNo}"})
  public ResponseEntity<Room> updateRoom(final String roomNo,
                                         @RequestBody @Valid final RoomUpdateDTO roomUpdateDto) {
    log.info("Room update request received with roomNo={}, and body={}", roomNo, roomUpdateDto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping({"{roomNo}"})
  public ResponseEntity<Void> deleteRoom(@PathVariable final String roomNo) {
    log.info("Room deletion request received with roomNo={}", roomNo);
    return ResponseEntity.ok().build();
  }

}

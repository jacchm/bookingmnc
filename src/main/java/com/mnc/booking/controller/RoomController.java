package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.RoomCreationDTO;
import com.mnc.booking.controller.dto.RoomUpdateDTO;
import com.mnc.booking.model.Room;
import com.mnc.booking.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("booking")
@RestController
public class RoomController {

  private final RoomService roomService;

  @GetMapping({"{roomNo}"})
  public ResponseEntity<Room> getRoom(@PathVariable final String roomNo) {
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<Room>> getRooms() {
    return ResponseEntity.ok(List.of());
  }

  @PostMapping
  public ResponseEntity<String> createRoom(@Valid @RequestBody final RoomCreationDTO roomCreationDTO) {
    return ResponseEntity.ok(roomService.createRoom(roomCreationDTO));
  }

  @DeleteMapping({"{roomNo}"})
  public ResponseEntity<Void> deleteRoom(@PathVariable final String roomNo) {
    return ResponseEntity.ok().build();
  }

  @PutMapping({"{roomNo}"})
  public ResponseEntity<Room> deleteRoom(final String roomNo,
                                         @RequestBody @Valid final RoomUpdateDTO roomUpdateDto) {
    return ResponseEntity.ok().build();
  }

}

package com.mnc.booking.service;

import com.mnc.booking.controller.dto.room.RoomCreationDTO;
import com.mnc.booking.controller.dto.room.RoomDTO;
import com.mnc.booking.controller.dto.room.URIDTO;
import com.mnc.booking.exception.AlreadyExistsException;
import com.mnc.booking.exception.NotFoundException;
import com.mnc.booking.mapper.RoomMapper;
import com.mnc.booking.mapper.URIMapper;
import com.mnc.booking.model.Room;
import com.mnc.booking.model.URI;
import com.mnc.booking.repository.RoomRepository;
import com.mnc.booking.repository.URIRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {

  private static final String ROOM_ALREADY_EXISTS_ERROR_MSG = "Room with roomNo=%s already exists.";
  private static final String ROOM_NOT_FOUND_ERROR_MSG = "Room with roomNo=%s has not been found.";

  private final RoomRepository roomRepository;
  private final URIRepository uriRepository;
  private final RoomMapper roomMapper;
  private final URIMapper uriMapper;

  public String createRoom(final RoomCreationDTO roomCreationDTO) {
    final Room newRoom = roomMapper.mapToRoom(roomCreationDTO);

    if (roomRepository.existsById(roomCreationDTO.getRoomNo())) {
      throw new AlreadyExistsException(String.format(ROOM_ALREADY_EXISTS_ERROR_MSG, roomCreationDTO.getRoomNo()));
    }
    final Room savedRoom = roomRepository.save(newRoom);
    return savedRoom.getRoomNo();
  }

  public RoomDTO getRoom(final String roomNo) {
    return roomRepository.findById(roomNo)
        .map(roomMapper::mapToRoomDTO)
        .orElseThrow(() -> new NotFoundException(String.format(ROOM_NOT_FOUND_ERROR_MSG, roomNo)));
  }

  // TODO: implement URI management later
  public Integer addUri(final URIDTO uriDto) {
    final URI newUri = uriMapper.mapToURI(uriDto);

    final URI savedUri = uriRepository.save(newUri);
    return savedUri.getId();

  }
}

package com.mnc.booking.service;

import com.mnc.booking.controller.dto.RoomCreationDTO;
import com.mnc.booking.controller.dto.URIDTO;
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

  private final RoomRepository roomRepository;
  private final URIRepository uriRepository;
  private final RoomMapper roomMapper;
  private final URIMapper uriMapper;

  public String createRoom(final RoomCreationDTO roomCreationDTO) {
    final Room newRoom = roomMapper.mapToRoom(roomCreationDTO);

    final Room savedRoom = roomRepository.save(newRoom);
    return savedRoom.getRoomNo();
  }

  // TODO: implement URI management later
  public Integer addUri(final URIDTO uriDto) {
    final URI newUri = uriMapper.mapToURI(uriDto);

    final URI savedUri = uriRepository.save(newUri);
    return savedUri.getId();

  }
}

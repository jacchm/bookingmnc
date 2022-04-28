package com.mnc.booking.service;

import com.mnc.booking.controller.dto.RoomCreationDTO;
import com.mnc.booking.exception.BadRequestException;
import com.mnc.booking.mapper.RoomMapper;
import com.mnc.booking.model.Room;
import com.mnc.booking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {

  private static final String USER_LOG_ERROR_MSG = """
      Room with roomNo={} has not been {}. Reason={}""";
  private static final String REPOSITORY_ERROR_MSG = """
      An error occurred while trying to connect to the database.""";

  private final RoomRepository roomRepository;
  private final RoomMapper roomMapper;

  public String createRoom(final RoomCreationDTO roomCreationDTO) {
    final Room newRoom = roomMapper.mapToRoom(roomCreationDTO);

    final Room savedRoom = roomRepository.save(newRoom);
    return savedRoom.getRoomNo();
  }
}

package com.mnc.booking.service;

import com.mnc.booking.controller.dto.room.RoomCreationDTO;
import com.mnc.booking.controller.dto.room.RoomDTO;
import com.mnc.booking.controller.dto.room.URIDTO;
import com.mnc.booking.controller.util.FilterParamsParser;
import com.mnc.booking.controller.util.SortParamsParser;
import com.mnc.booking.exception.AlreadyExistsException;
import com.mnc.booking.exception.NotFoundException;
import com.mnc.booking.mapper.RoomMapper;
import com.mnc.booking.mapper.URIMapper;
import com.mnc.booking.model.Room;
import com.mnc.booking.model.URI;
import com.mnc.booking.repository.RoomRepository;
import com.mnc.booking.repository.URIRepository;
import com.mnc.booking.util.GenericSpecificationsBuilder;
import com.mnc.booking.util.SpecificationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
  private final FilterParamsParser filterParamsParser;
  private final SortParamsParser sortParamsParser;
  private final SpecificationFactory<Room> roomSpecificationFactory;

  public String createRoom(final RoomCreationDTO roomCreationDTO) {
    final Room newRoom = roomMapper.mapToRoom(roomCreationDTO);

    if (roomRepository.existsById(roomCreationDTO.getRoomNo())) {
      throw new AlreadyExistsException(String.format(ROOM_ALREADY_EXISTS_ERROR_MSG, roomCreationDTO.getRoomNo()));
    }
    final Room savedRoom = roomRepository.save(newRoom);
    return savedRoom.getRoomNo();
  }

  private Specification<Room> buildFilteringQuery(final Map<String, String> filters) {
    final GenericSpecificationsBuilder<Room> builder = new GenericSpecificationsBuilder<>();
    if(!CollectionUtils.isEmpty(filters)) {
      filters.forEach((key, value) -> builder.with(roomSpecificationFactory.isEqual(key, value)));
    }

    return builder.build();
  }

  public Page<Room> searchRooms(final Integer pageNumber, final Integer pageSize, final String sortParams, final Map<String, String> filterParams) {
    final Map<String, String> filters = filterParamsParser.prepareFilterParamsMap(filterParams, Room.class);
    final Sort sort = Sort.by(sortParamsParser.prepareSortOrderList(sortParams, Room.class));
    final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
    return roomRepository.findAll(buildFilteringQuery(filters), pageable);
  }

  public RoomDTO getRoom(final String roomNo) {
    return roomRepository.findById(roomNo)
        .map(roomMapper::mapToRoomDTO)
        .orElseThrow(() -> new NotFoundException(String.format(ROOM_NOT_FOUND_ERROR_MSG, roomNo)));
  }

  public List<RoomDTO> getRooms(final Integer pageNumber, final Integer pageSize, final String sort) {
    final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.unsorted());
    return roomRepository.findAll(pageable)
                         .getContent()
                         .stream()
                         .map(roomMapper::mapToRoomDTO)
                         .collect(Collectors.toList());
  }

  // TODO: implement URI management later
  public Integer addUri(final URIDTO uriDto) {
    final URI newUri = uriMapper.mapToURI(uriDto);

    final URI savedUri = uriRepository.save(newUri);
    return savedUri.getId();

  }

  public void deleteRoom(final String roomNo) {
    roomRepository.deleteById(roomNo);
  }
}

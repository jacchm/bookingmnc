package com.mnc.booking.service;

import com.mnc.booking.controller.dto.room.RoomCreationDTO;
import com.mnc.booking.controller.dto.room.RoomDTO;
import com.mnc.booking.controller.dto.room.RoomFilterParams;
import com.mnc.booking.controller.dto.room.URIDTO;
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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

  public Page<Room> searchRooms(final Integer pageNumber, final Integer pageSize, final String sortParams, final RoomFilterParams filterParams) {
    final Sort sort = Sort.by(sortParamsParser.prepareSortOrderList(sortParams, Room.class));
    final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

    return roomRepository.findAll(buildFilteringQuery(filterParams), pageable);
  }

  public Page<Room> findAvailableRooms(final Integer pageNumber, final Integer pageSize, final String sortParams, final RoomFilterParams filterParams) {
    final Sort sort = Sort.by(sortParamsParser.prepareSortOrderList(sortParams, Room.class));
    final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

    // TODO: add the reservation service to fetch only available rooms within date range
    return roomRepository.findAll(buildFilteringQuery(filterParams), pageable);
  }

  public void updateRoom(final String roomNo, final Room roomUpdate) {
    final Room room = roomRepository.findById(roomNo)
        .orElseThrow(() -> new NotFoundException(String.format(ROOM_NOT_FOUND_ERROR_MSG, roomNo)));
    BeanUtils.copyProperties(roomUpdate, room, ignoreNullProperties(roomUpdate));

    roomRepository.save(room);
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

  public void deleteUri(final Integer uriId) {
    uriRepository.deleteById(uriId);
  }

  public void deleteRoom(final String roomNo) {
    roomRepository.deleteById(roomNo);
  }

  private Specification<Room> buildFilteringQuery(final RoomFilterParams filters) {
    final GenericSpecificationsBuilder<Room> builder = new GenericSpecificationsBuilder<>();
    if (Objects.nonNull(filters)) {
      ReflectionUtils.doWithFields(RoomFilterParams.class,
          field -> {
            field.setAccessible(true);
            if (Objects.nonNull(field.get(filters))) {
              if (field.getName().equals("noPeople") || field.getName().equals("roomSizeValue")) {
                builder.with(roomSpecificationFactory.isGreaterThanOrEqualTo(field.getName(), (Integer) field.get(filters), builder));
              } else if (field.getName().equals("pricePerNightValue")) {
                builder.with(roomSpecificationFactory.isLessThanOrEqualTo(field.getName(), (Integer) field.get(filters), builder));
              } else {
                builder.with(roomSpecificationFactory.isEqual(field.getName(), field.get(filters), builder));
              }
            }
          });
    }
    return builder.build();
  }

  private String[] ignoreNullProperties(final Room roomUpdate) {
    final List<String> ignoredProperties = new ArrayList<>();
    ReflectionUtils.doWithFields(Room.class,
        field -> {
          if (Objects.isNull(field.get(roomUpdate))) {
            ignoredProperties.add(field.getName());
          }
        });
    return ignoredProperties.toArray(String[]::new);
  }
}

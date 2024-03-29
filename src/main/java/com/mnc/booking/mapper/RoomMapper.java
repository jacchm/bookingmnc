package com.mnc.booking.mapper;

import com.mnc.booking.controller.dto.room.RoomCreationDTO;
import com.mnc.booking.controller.dto.room.RoomDTO;
import com.mnc.booking.controller.dto.room.RoomUpdateDTO;
import com.mnc.booking.model.Room;
import com.mnc.booking.model.URI;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
    uses = {PriceMapper.class, MeasurementUnitMapper.class, URIMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

  @Mapping(source = "pricePerNight.value", target = "pricePerNightValue")
  @Mapping(source = "pricePerNight.currency", target = "pricePerNightCurrency")
  @Mapping(source = "roomSize.value", target = "roomSizeValue")
  @Mapping(source = "roomSize.unit", target = "roomSizeUnit")
  @Mapping(target = "images", expression = "java(RoomMapper.mapToRoomURIs(roomCreationDTO))")
  Room mapToRoom(final RoomCreationDTO roomCreationDTO);

  @Mapping(source = "pricePerNight.value", target = "pricePerNightValue")
  @Mapping(source = "pricePerNight.currency", target = "pricePerNightCurrency")
  @Mapping(source = "roomSize.value", target = "roomSizeValue")
  @Mapping(source = "roomSize.unit", target = "roomSizeUnit")
  Room mapToRoom(final RoomUpdateDTO roomUpdateDTO);

  @Mapping(source = "pricePerNightValue", target = "pricePerNight.value")
  @Mapping(source = "pricePerNightCurrency", target = "pricePerNight.currency")
  @Mapping(source = "roomSizeValue", target = "roomSize.value")
  @Mapping(source = "roomSizeUnit", target = "roomSize.unit")
  RoomDTO mapToRoomDTO(final Room room);

  static List<URI> mapToRoomURIs(final RoomCreationDTO roomCreationDTO) {
    return roomCreationDTO.getImages().stream()
        .map(image -> new URI(null, roomCreationDTO.getRoomNo(), image.getUri()))
        .collect(Collectors.toList());
  }

}

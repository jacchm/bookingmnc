package com.mnc.booking.mapper;

import com.mnc.booking.controller.dto.RoomCreationDTO;
import com.mnc.booking.controller.dto.RoomDTO;
import com.mnc.booking.model.Room;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {PriceMapper.class, MeasurementUnitMapper.class, URIMapper.class, MetadataMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoomMapper {

  Room mapToRoom(final RoomCreationDTO roomCreationDTO);

  RoomDTO mapToRoomDTO(final Room room);

}

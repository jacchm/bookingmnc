package com.mnc.booking.mapper;

import com.mnc.booking.controller.dto.room.RoomCreationDTO;
import com.mnc.booking.controller.dto.room.RoomDTO;
import com.mnc.booking.model.Room;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    uses = {PriceMapper.class, MeasurementUnitMapper.class, URIMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

  Room mapToRoom(final RoomCreationDTO roomCreationDTO);

  RoomDTO mapToRoomDTO(final Room room);

}

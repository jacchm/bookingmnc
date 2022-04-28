package com.mnc.booking.mapper;

import com.mnc.booking.controller.dto.RoomCreationDTO;
import com.mnc.booking.model.Room;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

  Room mapToRoom(final RoomCreationDTO roomCreationDTO);

}

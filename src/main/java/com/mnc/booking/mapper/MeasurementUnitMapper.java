package com.mnc.booking.mapper;

import com.mnc.booking.controller.dto.room.MeasurementUnitDTO;
import com.mnc.booking.model.MeasurementUnit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MeasurementUnitMapper {

  MeasurementUnit mapToMeasurementUnit(final MeasurementUnitDTO measurementUnitDTO);

  MeasurementUnitDTO mapToPriceDTO(final MeasurementUnit measurementUnit);
}

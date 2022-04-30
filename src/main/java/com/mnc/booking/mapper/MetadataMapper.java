package com.mnc.booking.mapper;

import com.mnc.booking.controller.dto.MetadataDTO;
import com.mnc.booking.model.Metadata;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetadataMapper {

  Metadata mapToPrice(final MetadataDTO metadataDTO);

  MetadataDTO mapToPriceDTO(final Metadata metadata);

}

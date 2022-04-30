package com.mnc.booking.mapper;

import com.mnc.booking.controller.dto.room.PriceDTO;
import com.mnc.booking.model.Price;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {

  Price mapToPrice(final PriceDTO priceDTO);

  PriceDTO mapToPriceDTO(final Price price);

}

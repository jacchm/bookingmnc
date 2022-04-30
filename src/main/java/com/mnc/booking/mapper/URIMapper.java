package com.mnc.booking.mapper;

import com.mnc.booking.controller.dto.room.URIDTO;
import com.mnc.booking.model.URI;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface URIMapper {
  
  URI mapToURI(final URIDTO uridto);

  URIDTO mapToURIDTO(final URI uri);

}

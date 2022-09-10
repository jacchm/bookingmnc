package com.mnc.booking.mapper;

import com.mnc.booking.controller.dto.room.URIDTO;
import com.mnc.booking.model.URI;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface URIMapper {

  @Mapping(target = "id", ignore = true)
  URI mapToURI(final URIDTO uridto);

  URIDTO mapToURIDTO(final URI uri);

}

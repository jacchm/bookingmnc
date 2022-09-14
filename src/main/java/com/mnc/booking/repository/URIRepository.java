package com.mnc.booking.repository;

import com.mnc.booking.model.URI;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface URIRepository extends CrudRepository<URI, Integer> {

  @Transactional
  void deleteByIdAndAndRoomNo(final Integer id, final String RoomNo);
}

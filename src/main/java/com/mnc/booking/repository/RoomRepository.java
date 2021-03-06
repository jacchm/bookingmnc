package com.mnc.booking.repository;

import com.mnc.booking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, String>, JpaSpecificationExecutor<Room> {

  @Override
  @Modifying
  @Query(nativeQuery = true, value = "DELETE FROM room WHERE room_no = ?1")
  void deleteById(final String roomNo);

}

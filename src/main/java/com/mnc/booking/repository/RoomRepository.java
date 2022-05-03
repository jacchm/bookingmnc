package com.mnc.booking.repository;

import com.mnc.booking.model.Room;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoomRepository extends PagingAndSortingRepository<Room, String> {

    @Override
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM rooms WHERE room_no = ?1")
    void deleteById(final String roomNo);

}

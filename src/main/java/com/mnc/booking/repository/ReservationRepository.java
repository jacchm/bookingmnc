package com.mnc.booking.repository;

import com.mnc.booking.model.Reservation;
import com.mnc.booking.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

  @Query("SELECT COUNT(r) FROM reservation r WHERE " +
      "r.roomNo = ?3 " +
      "and " +
      "(r.dateFrom < ?2 and r.dateTo > ?1 or r.dateTo < ?2 and r.dateFrom > ?1) or (r.dateFrom < ?1 and r.dateTo > ?2)")
  long findReservationByDateRange(final Instant dateFrom, final Instant dateTo, final String roomNo);

  Optional<Reservation> findByIdAndUsername(final Long id, final String username);

  @Modifying
  @Query("update reservation r set r.status = ?2 where r.id = ?1")
  void setReservationStatus(final Long id, final ReservationStatus reservationStatus);

}

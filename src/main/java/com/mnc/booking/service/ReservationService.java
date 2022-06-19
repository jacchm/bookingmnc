package com.mnc.booking.service;

import com.mnc.booking.controller.util.SortParamsParser;
import com.mnc.booking.mapper.ReservationMapper;
import com.mnc.booking.model.Room;
import com.mnc.booking.util.SpecificationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

  private static final String RESERVATION_NOT_FOUND_ERROR_MSG = "Reservation with id=%s has not been found.";


  private final SortParamsParser sortParamsParser;
  private final SpecificationFactory<Room> roomSpecificationFactory;
  private final ReservationMapper reservationMapper;

}

package com.yuryuu.libraryproject.service.reservation;

import com.yuryuu.libraryproject.dto.reservation.ReservationDTO;

public interface ReservationService {
    Boolean addReservation(ReservationDTO reservationDTO);
    Boolean editReservation(ReservationDTO reservationDTO);
    void deleteReservation(Long reservationNo);
    ReservationDTO getReservation(Long reservationNo);
}

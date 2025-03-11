package com.yuryuu.libraryproject.repository.reservation;

import com.yuryuu.libraryproject.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}

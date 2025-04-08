package com.yuryuu.libraryproject.controller.reservation;

import com.yuryuu.libraryproject.dto.reservation.ReservationDTO;
import com.yuryuu.libraryproject.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
@Log4j2
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/add")
    public Boolean addReservation(@Valid @RequestBody ReservationDTO reservationDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return reservationService.addReservation(reservationDTO);
    }

    @PutMapping("/update")
    public Boolean updateReservation(@Valid @RequestBody ReservationDTO reservationDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return reservationService.updateReservation(reservationDTO);
    }

    @DeleteMapping("/delete/{ReservationNo}")
    public void deleteReservation(@PathVariable("ReservationNo") Long reservationNo) {
        reservationService.deleteReservation(reservationNo);
    }

    @GetMapping("/{ReservationNo}/")
    public ReservationDTO getReservation(@PathVariable("ReservationNo") Long reservationNo) {
        return reservationService.getReservation(reservationNo);
    }
}

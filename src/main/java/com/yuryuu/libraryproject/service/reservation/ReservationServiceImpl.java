package com.yuryuu.libraryproject.service.reservation;

import com.yuryuu.libraryproject.domain.Reservation;
import com.yuryuu.libraryproject.dto.reservation.ReservationDTO;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import com.yuryuu.libraryproject.repository.member.MemberRepository;
import com.yuryuu.libraryproject.repository.reservation.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    private Reservation convertToReservation(ReservationDTO reservationDTO) {
        return Reservation.builder()
                .reservationNo(reservationDTO.getReservationNo())
                .book(bookRepository.findById(reservationDTO.getBookNo()).orElseThrow())
                .member(memberRepository.findById(reservationDTO.getMemberNo()).orElseThrow())
                .build();
    }
    private ReservationDTO convertToReservationDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .reservationNo(reservation.getReservationNo())
                .bookNo(reservation.getBook().getBookNo())
                .memberNo(reservation.getMember().getMemberNo())
                .build();
    }

    @Override
    public Boolean addReservation(ReservationDTO reservationDTO) {
        if (reservationDTO.getReservationNo() != null) return false;
        Reservation reservation = convertToReservation(reservationDTO);
        bookRepository.findById(reservationDTO.getBookNo()).ifPresent(book -> {book.reserveBook(reservation);});
        memberRepository.findById(reservationDTO.getMemberNo()).ifPresent(member -> {member.reserveBook(reservation);});
        reservationRepository.save(reservation);
        return true;
    }

    @Override
    public Boolean editReservation(ReservationDTO reservationDTO) {
        if (reservationDTO.getReservationNo() == null) return false;
        Reservation reservation = convertToReservation(reservationDTO);
        Reservation result = reservationRepository.save(reservation);
        return result.getReservationNo().equals(reservation.getReservationNo());
    }

    @Override
    public void deleteReservation(Long reservationNo) {
        reservationRepository.deleteById(reservationNo);
    }

    @Override
    public ReservationDTO getReservation(Long reservationNo) {
        Reservation result = reservationRepository.findById(reservationNo).orElseThrow(()-> new EntityNotFoundException("reservation not found"));
        return convertToReservationDTO(result);
    }
}

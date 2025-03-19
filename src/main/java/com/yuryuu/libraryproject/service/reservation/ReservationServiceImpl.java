package com.yuryuu.libraryproject.service.reservation;

import com.yuryuu.libraryproject.domain.Reservation;
import com.yuryuu.libraryproject.dto.reservation.ReservationDTO;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import com.yuryuu.libraryproject.repository.member.MemberRepository;
import com.yuryuu.libraryproject.repository.reservation.ReservationRepository;
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
        //Todo 여기부터
        return null;
    }

    @Override
    public Boolean editReservation(ReservationDTO reservationDTO) {
        return null;
    }

    @Override
    public Boolean deleteReservation(Long reservationNo) {
        return null;
    }

    @Override
    public Boolean checkReservation(Long reservationNo) {
        return null;
    }
}

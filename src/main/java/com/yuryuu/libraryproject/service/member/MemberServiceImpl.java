package com.yuryuu.libraryproject.service.member;

import com.yuryuu.libraryproject.domain.Member;
import com.yuryuu.libraryproject.domain.Request;
import com.yuryuu.libraryproject.domain.Reservation;
import com.yuryuu.libraryproject.domain.Review;
import com.yuryuu.libraryproject.dto.member.MemberDTO;
import com.yuryuu.libraryproject.repository.member.MemberRepository;
import com.yuryuu.libraryproject.repository.request.RequestRepository;
import com.yuryuu.libraryproject.repository.reservation.ReservationRepository;
import com.yuryuu.libraryproject.repository.review.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final RequestRepository requestRepository;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    private Member dtoToEntity(MemberDTO memberDTO) {
        Set<Request> requests = (memberDTO.getRequestNos() != null && !memberDTO.getRequestNos().isEmpty())
                ? new HashSet<>(requestRepository.findAllById(memberDTO.getRequestNos()))
                : null;
        Set<Review> reviews = (memberDTO.getReviewNos() != null && !memberDTO.getReviewNos().isEmpty())
                ? new HashSet<>(reviewRepository.findAllById(memberDTO.getReviewNos()))
                : null;
        Set<Reservation> reservations = (memberDTO.getReservationNos() != null && !memberDTO.getReservationNos().isEmpty())
                ? new HashSet<>(reservationRepository.findAllById(memberDTO.getReservationNos()))
                : null;
        return Member.builder()
                .memberNo(memberDTO.getMemberNo())
                .email(memberDTO.getEmail())
                .password(memberDTO.getPassword())
                .reviews(reviews)
                .requests(requests)
                .reservations(reservations)
                .build();
    }
    private MemberDTO entityToDTO(Member member) {
        Set<Long> requestNos = (member.getRequests() != null && !member.getRequests().isEmpty()) ? member.getRequests().stream().map(Request::getRequestNo).collect(Collectors.toSet()) : null;
        Set<Long> reviewNos = (member.getReviews() != null && !member.getReviews().isEmpty()) ? member.getReviews().stream().map(Review::getReviewNo).collect(Collectors.toSet()): null;
        Set<Long> reservationNos = (member.getReservations() != null && !member.getReservations().isEmpty()) ? member.getReservations().stream().map(Reservation::getReservationNo).collect(Collectors.toSet()) : null;
        return MemberDTO.builder()
                .memberNo(member.getMemberNo())
                .email(member.getEmail())
                .password(member.getPassword())
                .requestNos(requestNos)
                .reviewNos(reviewNos)
                .reservationNos(reservationNos)
                .build();
    }
    @Override
    @Transactional
    public Boolean addMember(MemberDTO memberDTO) {
        if (memberDTO.getMemberNo() != null) return false;
        Member result = memberRepository.save(dtoToEntity(memberDTO));
        return result.getEmail().equals(memberDTO.getEmail());
    }

    @Override
    public Boolean updateMember(MemberDTO memberDTO) {
        if (memberDTO.getMemberNo() == null) return false;
        Member result = memberRepository.save(dtoToEntity(memberDTO));
        return result.getEmail().equals(memberDTO.getEmail());
    }

    @Override
    public void deleteMember(Long memberNo) {
        memberRepository.deleteById(memberNo);
    }

    @Override
    public MemberDTO getMember(Long memberNo) {
        return entityToDTO(memberRepository.findById(memberNo).orElseThrow(() -> new EntityNotFoundException("member not found")));
    }
}

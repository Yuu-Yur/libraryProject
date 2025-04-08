package com.yuryuu.libraryproject.service.review;

import com.yuryuu.libraryproject.domain.Review;
import com.yuryuu.libraryproject.dto.PageRequestDTO;
import com.yuryuu.libraryproject.dto.PageResponseDTO;
import com.yuryuu.libraryproject.dto.review.ReviewDTO;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import com.yuryuu.libraryproject.repository.member.MemberRepository;
import com.yuryuu.libraryproject.repository.review.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    private Review dtoToEntity(ReviewDTO reviewDTO) {
        return Review.builder()
                .reviewNo(reviewDTO.getReviewNo())
                .rating(reviewDTO.getRating())
                .good(reviewDTO.getGood())
                .bad(reviewDTO.getBad())
                .content(reviewDTO.getContent())
                .book(bookRepository.findById(reviewDTO.getBookNo()).orElseThrow(() -> new EntityNotFoundException("book not found")))
                .member(memberRepository.findById(reviewDTO.getMemberNo()).orElseThrow(() -> new EntityNotFoundException("member not found")))
                .build();
    }
    private ReviewDTO entityToDto(Review review) {
        return ReviewDTO.builder()
                .reviewNo(review.getReviewNo())
                .rating(review.getRating())
                .good(review.getGood())
                .bad(review.getBad())
                .content(review.getContent())
                .bookNo(review.getBook().getBookNo())
                .memberNo(review.getMember().getMemberNo())
                .build();
    }
    @Override
    public Boolean addReview(ReviewDTO reviewDTO) {
        if (reviewDTO.getReviewNo() != null) return false;
        Review result = reviewRepository.save(dtoToEntity(reviewDTO));
        return result.getMember().getMemberNo().equals(reviewDTO.getMemberNo());
    }

    @Override
    public Boolean updateReview(ReviewDTO reviewDTO) {
        if (reviewDTO.getReviewNo() == null) return false;
        Review result = reviewRepository.save(dtoToEntity(reviewDTO));
        return result.getMember().getMemberNo().equals(reviewDTO.getMemberNo());
    }

    @Override
    public void deleteReview(Long reviewNo) {
        reviewRepository.deleteById(reviewNo);
    }

    @Override
    public ReviewDTO getReview(Long reviewNo) {
        Review result = reviewRepository.findById(reviewNo).orElseThrow(() -> new EntityNotFoundException("review not found"));
        return entityToDto(result);
    }

    @Override
    @Transactional
    public void calculateAvgRating(Long bookNo) {
        bookRepository.findById(bookNo).orElseThrow(() -> new EntityNotFoundException("book not found"))
                .changeAvgRating(reviewRepository.getAvgRating(bookNo));
    }

    @Override
    public PageResponseDTO<ReviewDTO> getBadReview(Integer badCount, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable();
        Page<Review> result = reviewRepository.getBadReviews(badCount, pageable);
        List<ReviewDTO> dtoList = result.getContent()
                .stream()
                .map(this::entityToDto)
                .toList();
        return PageResponseDTO.<ReviewDTO>builder()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .total(result.getTotalElements()).build();
    }
}

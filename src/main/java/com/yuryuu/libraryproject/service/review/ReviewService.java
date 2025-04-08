package com.yuryuu.libraryproject.service.review;

import com.yuryuu.libraryproject.dto.PageRequestDTO;
import com.yuryuu.libraryproject.dto.PageResponseDTO;
import com.yuryuu.libraryproject.dto.review.ReviewDTO;

public interface ReviewService {
    Boolean addReview(ReviewDTO reviewDTO);
    Boolean updateReview(ReviewDTO reviewDTO);
    void deleteReview(Long reviewNo);
    ReviewDTO getReview(Long reviewNo);
    void calculateAvgRating(Long bookNo);
    PageResponseDTO<ReviewDTO> getBadReview(Integer badCount, PageRequestDTO pageRequestDTO);
}

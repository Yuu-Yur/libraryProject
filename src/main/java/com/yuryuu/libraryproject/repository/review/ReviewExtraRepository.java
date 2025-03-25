package com.yuryuu.libraryproject.repository.review;

import com.yuryuu.libraryproject.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewExtraRepository {
    Float getAvgRating(Long bookNo);
    Page<Review> getBadReviews(Integer num, Pageable pageable);
}

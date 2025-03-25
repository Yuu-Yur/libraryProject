package com.yuryuu.libraryproject.repository.review;

import com.yuryuu.libraryproject.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewExtraRepository {
}

package com.yuryuu.libraryproject.repository.review;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.yuryuu.libraryproject.domain.QBook;
import com.yuryuu.libraryproject.domain.QReview;
import com.yuryuu.libraryproject.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewExtraRepositoryImpl extends QuerydslRepositorySupport implements ReviewExtraRepository {
    public ReviewExtraRepositoryImpl() {
        super(Review.class);
    }

    @Override
    public Float getAvgRating(Long bookNo) {
        QReview review = QReview.review;
        QBook book = QBook.book;
        BooleanBuilder bb = new BooleanBuilder();
        bb.and(book.bookNo.eq(bookNo));
        Double result = from(review)
                .leftJoin(review.book, book)
                .where(bb)
                .select(review.rating.avg())
                .fetchOne();
        return result == null ? 0.0f : result.floatValue();
    }

    @Override
    public Page<Review> getBadReviews(Integer num, Pageable pageable) {
        QReview review = QReview.review;
        QBook book = QBook.book;
        BooleanBuilder bb = new BooleanBuilder();
        bb.and(review.bad.goe(num));
        JPQLQuery<Review> query = from(review)
                .where(bb)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
//        this.getQuerydsl().applyPagination(pageable, query);
        List<Review> result = query.fetch();
        long total = query.fetchCount();
        return new PageImpl<>(result, pageable, total);
    }
}

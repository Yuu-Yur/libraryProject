package com.yuryuu.libraryproject.repository.book;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.yuryuu.libraryproject.domain.Book;
import com.yuryuu.libraryproject.domain.QAuthor;
import com.yuryuu.libraryproject.domain.QBook;
import com.yuryuu.libraryproject.domain.QPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.sql.Date;

public class BookSearchRepositoryImpl extends QuerydslRepositorySupport implements BookSearchRepository {

    public BookSearchRepositoryImpl() {super(Book.class);}

    @Override
    public Page<Book> search(String[] types, String q, Integer rating, Date startDate, Date endDate, String KDC, Pageable pageable) {
        QBook book = QBook.book;
        QAuthor author = QAuthor.author;
        QPublisher publisher = QPublisher.publisher;
        JPQLQuery<Book> query = from(book);
        query.leftJoin(book.authors, author);
        query.leftJoin(publisher).on(book.publisher.publisherNo.eq(publisher.publisherNo));
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (types != null && types.length > 0 && q != null) {
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(book.title.contains(q));
                        break;
                    case "a" :
                        booleanBuilder.or(author.authorName.contains(q));
                        break;
                    case "p" :
                        booleanBuilder.or(publisher.publisherName.contains(q));
                }
            }
        }
        if (rating != null) {
            booleanBuilder.and(book.avrRating.goe(rating));
        }
        if (startDate != null && endDate != null) {
            booleanBuilder.and(book.releaseDate.between(startDate, endDate));
        }
        if (KDC != null) {
            booleanBuilder.and(book.kdc.startsWith(KDC));
        }
        query.where(booleanBuilder);
        //todo dto 로 변환해 줄지, entity 로 그대로 나갈지 고민 필요
//        // 이렇게 형변환 한 query 에 pagination 적용
//        this.getQuerydsl().applyPagination(pageable, dtoQuery);
//
//        // pagination 적용 된 query 실행해서 데이터를 list 로 받기
//        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
//        // count 로 총 갯수 받기
//        long total = dtoQuery.fetchCount();
//        // Page 타입(데이터 , 페이지형식, 총 갯수)에 담아서 return
//        return new PageImpl<BoardListReplyCountDTO>(dtoList, pageable, total);
        return null;
    }
}

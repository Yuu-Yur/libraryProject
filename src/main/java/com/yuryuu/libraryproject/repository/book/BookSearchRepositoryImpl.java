package com.yuryuu.libraryproject.repository.book;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.yuryuu.libraryproject.domain.Book;
import com.yuryuu.libraryproject.domain.QAuthor;
import com.yuryuu.libraryproject.domain.QBook;
import com.yuryuu.libraryproject.domain.QPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class BookSearchRepositoryImpl extends QuerydslRepositorySupport implements BookSearchRepository {

    public BookSearchRepositoryImpl() {super(Book.class);}

    @Override
    public Page<Book> search(String[] types, String q, int rating, Date startDate, Date endDate, String kdc, String isbn, Pageable pageable) {
        QBook book = QBook.book;
        QAuthor author = QAuthor.author;
        QPublisher publisher = QPublisher.publisher;
        JPQLQuery<Book> query = from(book);
        query.leftJoin(book.authors, author);
        query.leftJoin(book.publisher, publisher);
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
        if (rating > 0) {
            booleanBuilder.and(book.avrRating.goe(rating));
        }
        if (startDate != null && endDate != null) {
            booleanBuilder.and(book.releaseDate.between(startDate, endDate));
        }
        if (kdc != null) {
            booleanBuilder.and(book.kdc.startsWith(kdc));
        }
        if (isbn != null) {
            booleanBuilder.and(book.isbn.startsWith(isbn));
        }
        query.where(booleanBuilder);
        if (pageable != null) {
            this.getQuerydsl().applyPagination(pageable, query);
        }
        List<Book> entityList = query.fetch();
        long total = query.fetchCount();
        return new PageImpl<Book>(entityList, pageable, total);
    }
}

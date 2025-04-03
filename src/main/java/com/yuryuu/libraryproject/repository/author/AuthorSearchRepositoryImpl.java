package com.yuryuu.libraryproject.repository.author;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.yuryuu.libraryproject.domain.Author;
import com.yuryuu.libraryproject.domain.QAuthor;
import com.yuryuu.libraryproject.dto.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorSearchRepositoryImpl extends QuerydslRepositorySupport implements AuthorSearchRepository {
    public AuthorSearchRepositoryImpl() {super(Author.class);}


    @Override
    public Page<Author> searchAuthors(PageRequestDTO pageRequestDTO) {
        String q = pageRequestDTO.getKeyword().trim();
        Pageable pageable = pageRequestDTO.getPageable();
        QAuthor author = QAuthor.author;
        JPQLQuery<Author> query = from(author);
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(author.authorName.eq(q));
        query.where(builder);
        this.getQuerydsl().applyPagination(pageable, query);
        long total = query.fetchCount();
        List<Author> authors = query.fetch();
        return new PageImpl<Author>(authors, pageable, total);
    }
}

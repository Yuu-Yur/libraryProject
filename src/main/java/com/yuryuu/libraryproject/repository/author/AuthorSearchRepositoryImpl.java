package com.yuryuu.libraryproject.repository.author;

import com.yuryuu.libraryproject.domain.Author;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorSearchRepositoryImpl extends QuerydslRepositorySupport implements AuthorSearchRepository {
    public AuthorSearchRepositoryImpl() {super(Author.class);}


}

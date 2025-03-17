package com.yuryuu.libraryproject.repository.book;

import com.yuryuu.libraryproject.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;

public interface BookSearchRepository {
    Page<Book> search(String[] types, String q, int rating, Date startDate, Date endDate, String kdc, Pageable pageable);
}

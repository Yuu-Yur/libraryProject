package com.yuryuu.libraryproject.repository.book;

import com.yuryuu.libraryproject.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>, BookSearchRepository {
}

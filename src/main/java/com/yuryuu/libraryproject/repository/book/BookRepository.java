package com.yuryuu.libraryproject.repository.book;

import com.yuryuu.libraryproject.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, BookSearchRepository {
    @Query(value = "SELECT * from book ORDER BY release_date DESC", nativeQuery = true)
    Page<Book> newest(Pageable pageable);
    @Query(value = "SELECT * from book WHERE title = ?", nativeQuery = true)
    Optional<Book> findByTitle(String title);
}

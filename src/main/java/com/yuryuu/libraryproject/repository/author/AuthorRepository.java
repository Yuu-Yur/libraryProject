package com.yuryuu.libraryproject.repository.author;

import com.yuryuu.libraryproject.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorSearchRepository {
    @Query(value = "SELECT * FROM author WHERE author_name = ? LIMIT 1",nativeQuery = true)
    Optional<Author> findByAuthorName(String authorName);
}

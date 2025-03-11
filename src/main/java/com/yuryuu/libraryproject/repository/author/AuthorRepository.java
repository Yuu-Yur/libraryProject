package com.yuryuu.libraryproject.repository.author;

import com.yuryuu.libraryproject.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}

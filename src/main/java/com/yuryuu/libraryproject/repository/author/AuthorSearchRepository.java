package com.yuryuu.libraryproject.repository.author;

import com.yuryuu.libraryproject.domain.Author;
import com.yuryuu.libraryproject.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface AuthorSearchRepository {
    Page<Author> searchAuthors(PageRequestDTO pageRequestDTO);
}

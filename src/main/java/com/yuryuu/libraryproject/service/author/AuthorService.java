package com.yuryuu.libraryproject.service.author;

import com.yuryuu.libraryproject.dto.author.AuthorDTO;

public interface AuthorService {
    Boolean addAuthor(AuthorDTO authorDTO);
    Boolean updateAuthor(AuthorDTO authorDTO);
    void deleteAuthor(Long authorNo);
    AuthorDTO getAuthor(Long authorNo);
    void addBookToAuthor(Long authorNo, Long bookNo);
}

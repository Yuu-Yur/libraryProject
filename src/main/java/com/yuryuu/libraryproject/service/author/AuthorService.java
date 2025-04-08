package com.yuryuu.libraryproject.service.author;

import com.yuryuu.libraryproject.dto.PageRequestDTO;
import com.yuryuu.libraryproject.dto.PageResponseDTO;
import com.yuryuu.libraryproject.dto.author.AuthorDTO;

public interface AuthorService {
    AuthorDTO addAuthor(AuthorDTO authorDTO);
    Boolean updateAuthor(AuthorDTO authorDTO);
    void deleteAuthor(Long authorNo);
    AuthorDTO getAuthor(Long authorNo);
    PageResponseDTO<AuthorDTO> getAuthors(PageRequestDTO pageRequestDTO);
    PageResponseDTO<AuthorDTO> searchAuthors(PageRequestDTO pageRequestDTO);
    void setBookAuthor(Long authorNo, Long bookNo);
}

package com.yuryuu.libraryproject.service.book;

import com.yuryuu.libraryproject.dto.PageRequestDTO;
import com.yuryuu.libraryproject.dto.PageResponseDTO;
import com.yuryuu.libraryproject.dto.book.BookDTO;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;


    //TODO bookservice 작성, 을 위해 bookDTO 작성 필요
    @Override
    public String addBook(BookDTO bookDTO) {
        return null;
    }

    @Override
    public Boolean updateBook(BookDTO bookDTO) {
        return null;
    }

    @Override
    public Boolean deleteBook(Long bookNo) {
        return null;
    }

    @Override
    public BookDTO getBook(Long bookNo) {
        return null;
    }

    @Override
    public PageResponseDTO<BookDTO> getNewBook(PageRequestDTO pageRequestDTO) {
        return null;
    }

    @Override
    public PageResponseDTO<BookDTO> searchBooks(PageRequestDTO pageRequestDTO) {
        return null;
    }
}

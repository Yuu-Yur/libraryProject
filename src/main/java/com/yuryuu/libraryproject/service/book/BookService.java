package com.yuryuu.libraryproject.service.book;

import com.yuryuu.libraryproject.dto.PageRequestDTO;
import com.yuryuu.libraryproject.dto.PageResponseDTO;
import com.yuryuu.libraryproject.dto.book.BookDTO;
import com.yuryuu.libraryproject.dto.book.BookStringDTO;

public interface BookService {
    public String addBook(BookStringDTO bookStringDTO); // 책 제목 반환 예정
    public Boolean updateBook(BookDTO bookDTO); // 수정 여부 체크
    public void deleteBook(Long bookNo); // 삭제 여부 체크
    public BookDTO getBook(Long bookNo); // 책 상세 정보 반환 예정
    public PageResponseDTO<BookDTO> getNewBook(PageRequestDTO pageRequestDTO); // 신간만이면 될듯
    public PageResponseDTO<BookDTO> searchBooks(PageRequestDTO pageRequestDTO); // 검색
}

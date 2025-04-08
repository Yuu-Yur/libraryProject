package com.yuryuu.libraryproject.controller.book;

import com.yuryuu.libraryproject.domain.Member;
import com.yuryuu.libraryproject.dto.PageRequestDTO;
import com.yuryuu.libraryproject.dto.PageResponseDTO;
import com.yuryuu.libraryproject.dto.book.BookDTO;
import com.yuryuu.libraryproject.dto.book.BookStringDTO;
import com.yuryuu.libraryproject.service.book.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Log4j2
public class BookController {
    private final BookService bookService;

    @GetMapping("/new")
    public PageResponseDTO<BookDTO> newBookList(Member member, PageRequestDTO pageRequestDTO) {
        return bookService.getNewBook(pageRequestDTO);
    }

    @GetMapping("/search")
    public PageResponseDTO<BookDTO> searchBooks(Member member, PageRequestDTO pageRequestDTO) {
        log.info("키워드" + pageRequestDTO.getKeyword());
        return bookService.searchBooks(pageRequestDTO);
    }

    @GetMapping("/{bookNo}")
    public BookDTO getBook(@PathVariable Long bookNo) {
        return bookService.getBook(bookNo);
    }

    @PostMapping("/add")
    public Boolean addBook(@Valid @RequestBody BookStringDTO bookStringDTO, BindingResult bindingResult, Member member) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return bookService.addBook(bookStringDTO);
    }

    @PutMapping("/update")
    public Boolean updateBook(@Valid @RequestBody BookDTO bookDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return bookService.updateBook(bookDTO);
    }

    @DeleteMapping("/delete/{bookNo}")
    public void deleteBook(@PathVariable Long bookNo) {
        bookService.deleteBook(bookNo);
    }

    @GetMapping("/borrow")
    public Boolean borrowBook(@RequestParam Long bookNo,@RequestParam Long memberNo) {
        return bookService.borrowBook(bookNo, memberNo);
    }

    @GetMapping("/return")
    public Boolean returnBook(@RequestParam Long bookNo,@RequestParam Long memberNo) {
        return bookService.returnBook(bookNo, memberNo);
    }
}

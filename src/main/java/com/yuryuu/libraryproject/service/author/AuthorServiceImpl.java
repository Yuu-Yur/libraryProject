package com.yuryuu.libraryproject.service.author;

import com.yuryuu.libraryproject.domain.Author;
import com.yuryuu.libraryproject.domain.Book;
import com.yuryuu.libraryproject.dto.PageRequestDTO;
import com.yuryuu.libraryproject.dto.PageResponseDTO;
import com.yuryuu.libraryproject.dto.author.AuthorDTO;
import com.yuryuu.libraryproject.repository.author.AuthorRepository;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

// 유저가 만질일 없음
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    private Author convertToAuthor(AuthorDTO authorDTO) {
        // 사실상 편집용, 추가시엔 그냥 빈 해시셋임
        Set<Book> books = new HashSet<>(bookRepository.findAllById(authorDTO.getBookNos()));
        return Author.builder()
                .authorNo(authorDTO.getAuthorNo())
                .authorName(authorDTO.getAuthorName())
                .books(books)
                .build();
    }
    private AuthorDTO convertToAuthorDTO(Author author) {
        return AuthorDTO.builder()
                .authorNo(author.getAuthorNo())
                .authorName(author.getAuthorName())
                .bookNos(author.getBooks().stream().map(Book::getBookNo).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public AuthorDTO addAuthor(AuthorDTO authorDTO) {
        if (authorDTO.getAuthorNo() != null) return null;
        Author result = authorRepository.save(convertToAuthor(authorDTO));
        if (!authorDTO.getBookNos().isEmpty()) {
            Set<Book> books = new HashSet<>(bookRepository.findAllById(authorDTO.getBookNos()));
            for (Book book : books) {
                book.getAuthors().add(result);
            }
            bookRepository.saveAll(books);
        }
        return convertToAuthorDTO(result);
    }

    @Override
    @Transactional
    public Boolean updateAuthor(AuthorDTO authorDTO) {
        if (authorDTO.getAuthorNo() == null) return false;

        // 1. 기존 author 조회
        Author author = authorRepository.findById(authorDTO.getAuthorNo())
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        // 2. 필드 수정
        author.setAuthorName(authorDTO.getAuthorName());

        // 3. 기존 book 관계 제거
        for (Book book : new HashSet<>(author.getBooks())) {
            book.getAuthors().remove(author);
        }
        author.getBooks().clear();

        // 4. 새 book 관계 추가
        Set<Book> newBooks = new HashSet<>(bookRepository.findAllById(authorDTO.getBookNos()));
        for (Book book : newBooks) {
            author.getBooks().add(book);
            book.getAuthors().add(author);
        }

        return true;
    }

    @Override
    public void deleteAuthor(Long authorNo) {
        authorRepository.deleteById(authorNo);
    }

    @Override
    @Transactional
    public AuthorDTO getAuthor(Long authorNo) {
        Author author = authorRepository.findById(authorNo).orElseThrow(() ->new EntityNotFoundException("Author not found"));
        return convertToAuthorDTO(author);
    }

    @Override
    public PageResponseDTO<AuthorDTO> getAuthors(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable();
        Page<Author> result = authorRepository.findAll(pageable);
        Page<AuthorDTO> convertedResult = result.map(this::convertToAuthorDTO);
        return PageResponseDTO.<AuthorDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(convertedResult.getContent())
                .total(convertedResult.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<AuthorDTO> searchAuthors(PageRequestDTO pageRequestDTO) {
        Page<Author> result = authorRepository.searchAuthors(pageRequestDTO);
        Page<AuthorDTO> convertedResult = result.map(this::convertToAuthorDTO);
        return PageResponseDTO.<AuthorDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(convertedResult.getContent())
                .total(convertedResult.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public void setBookAuthor(Long authorNo, Long bookNo) {
        Author author = authorRepository.findById(authorNo).orElseThrow(() ->new EntityNotFoundException("Author not found"));
        Book book = bookRepository.findById(bookNo).orElseThrow(() ->new EntityNotFoundException("Book not found"));
        author.getBooks().add(book);
        book.getAuthors().add(author);
        authorRepository.save(author);
        bookRepository.save(book);
    }
}

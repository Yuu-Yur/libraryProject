package com.yuryuu.libraryproject.service.author;

import com.yuryuu.libraryproject.domain.Author;
import com.yuryuu.libraryproject.domain.Book;
import com.yuryuu.libraryproject.dto.author.AuthorDTO;
import com.yuryuu.libraryproject.repository.author.AuthorRepository;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Boolean addAuthor(AuthorDTO authorDTO) {
        if (authorDTO.getAuthorNo() != null) return false;
        Author author = convertToAuthor(authorDTO);
        Author result = authorRepository.save(author);
        return result.getAuthorName().equals(authorDTO.getAuthorName());
    }

    @Override
    public Boolean updateAuthor(AuthorDTO authorDTO) {
        if (authorDTO.getAuthorNo() == null) return false;
        Author author = convertToAuthor(authorDTO);
        Author result = authorRepository.save(author);
        return result.getAuthorName().equals(authorDTO.getAuthorName());
    }

    @Override
    public void deleteAuthor(Long authorNo) {
        authorRepository.deleteById(authorNo);
    }

    @Override
    public AuthorDTO getAuthor(Long authorNo) {
        Author author = authorRepository.findById(authorNo).orElseThrow(() ->new EntityNotFoundException("Author not found"));
        return convertToAuthorDTO(author);
    }

    @Override
    public void addBookToAuthor(Long authorNo, Long bookNo) {
        Author a =authorRepository.findById(authorNo).orElseThrow(()->new EntityNotFoundException("Author not found"));
        Book b =bookRepository.findById(bookNo).orElseThrow(()->new EntityNotFoundException("Book not found"));
        a.getBooks().add(b);
        authorRepository.save(a);
    }
}

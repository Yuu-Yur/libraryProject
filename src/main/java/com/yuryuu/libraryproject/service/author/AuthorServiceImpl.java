package com.yuryuu.libraryproject.service.author;

import com.yuryuu.libraryproject.domain.Author;
import com.yuryuu.libraryproject.domain.Book;
import com.yuryuu.libraryproject.dto.author.AuthorDTO;
import com.yuryuu.libraryproject.repository.author.AuthorRepository;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        Set<Book> books = null;
        if (authorDTO.getAuthorNo() == null) {
            String authorName = authorDTO.getAuthorName();
            String[] types = {"a"};
            Page<Book> result = bookRepository.search(types, authorName, 0, null, null, null, null, null);
            books = new HashSet<>(result.getContent());
        } else {
            books = new HashSet<>(bookRepository.findAllById(authorDTO.getBookNos()));
        }
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
    public String addAuthor(AuthorDTO authorDTO) {
        Author author = convertToAuthor(authorDTO);
        Author result = authorRepository.save(author);
        return result.getAuthorName();
    }

    @Override
    public Boolean updateAuthor(AuthorDTO authorDTO) {
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
        Author author = authorRepository.findById(authorNo).orElseThrow(() ->new EntityNotFoundException("Book not found"));
        return convertToAuthorDTO(author);
    }
}

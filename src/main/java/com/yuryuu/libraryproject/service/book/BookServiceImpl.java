package com.yuryuu.libraryproject.service.book;

import com.yuryuu.libraryproject.domain.*;
import com.yuryuu.libraryproject.dto.PageRequestDTO;
import com.yuryuu.libraryproject.dto.PageResponseDTO;
import com.yuryuu.libraryproject.dto.book.BookDTO;
import com.yuryuu.libraryproject.repository.author.AuthorRepository;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import com.yuryuu.libraryproject.repository.member.MemberRepository;
import com.yuryuu.libraryproject.repository.publisher.PublisherRepository;
import com.yuryuu.libraryproject.repository.reservation.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;

    private Book convertToBook(BookDTO bookDTO) {
        Set<Author> authors = new HashSet<>(authorRepository.findAllById(bookDTO.getAuthorNos()));
        if (authors.size() != bookDTO.getAuthorNos().size()) {
            throw new EntityNotFoundException("Some authors not found");
        }
        Member member = null;
        Reservation reservation = null;
        if (bookDTO.getMemberNo() != null) {
            member = memberRepository.findById(bookDTO.getMemberNo()).orElseThrow(() -> new EntityNotFoundException("Member not found"));
        }
        if (bookDTO.getReservationNo() != null) {
            reservation = reservationRepository.findById(bookDTO.getReservationNo()).orElseThrow(() ->new EntityNotFoundException("Reservation not found"));
        }
        Publisher publisher = publisherRepository.findById(bookDTO.getPublisherNo()).orElseThrow(() ->new EntityNotFoundException("Publisher not found"));
        return Book.builder()
                .title(bookDTO.getTitle())
                .description(bookDTO.getDescription())
                .description(bookDTO.getDescription())
                .avrRating(bookDTO.getAvrRating())
                .releaseDate(bookDTO.getReleaseDate())
                .kdc(bookDTO.getKdc())
                .returnDate(bookDTO.getReturnDate())
                .authors(authors)
                .publisher(publisher)
                .member(member)
                .reservation(reservation)
                .build();
    }
    private BookDTO convertToBookDTO(Book book) {
        return BookDTO.builder()
                .bookNo(book.getBookNo())
                .title(book.getTitle())
                .description(book.getDescription())
                .avrRating(book.getAvrRating())
                .releaseDate(book.getReleaseDate())
                .kdc(book.getKdc())
                .returnDate(book.getReturnDate())
                .authorNos(book.getAuthors().stream().map(Author::getAuthorNo).collect(Collectors.toSet()))
                .publisherNo(book.getPublisher().getPublisherNo())
                .memberNo(book.getMember().getMemberNo())
                .reservationNo(book.getReservation().getReservationNo())
                .build();
    }

    //TODO ,bookservice 마지막searchBooks 작성
    @Override
    public String addBook(BookDTO bookDTO) throws EntityNotFoundException {
        Book book = convertToBook(bookDTO);
        Book result = bookRepository.save(book);

        return result.getTitle();
    }

    @Override
    public Boolean updateBook(BookDTO bookDTO) throws EntityNotFoundException {
        Book book = convertToBook(bookDTO);
        Book result = bookRepository.save(book);
        return result.getBookNo() != null;
    }

    @Override
    public void deleteBook(Long bookNo) {
        bookRepository.deleteById(bookNo);
    }

    @Override
    public BookDTO getBook(Long bookNo) {
        Book book = bookRepository.findById(bookNo).orElseThrow(() ->new EntityNotFoundException("Book not found"));
        return convertToBookDTO(book);
    }

    @Override
    public PageResponseDTO<BookDTO> getNewBook(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize());
        Page<Book> result = bookRepository.newest(pageable);
        List<BookDTO> dtoList = result.getContent().stream().map(this::convertToBookDTO).toList();
        return PageResponseDTO.<BookDTO>builder()
                .total((int)result.getTotalElements())
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public PageResponseDTO<BookDTO> searchBooks(PageRequestDTO pageRequestDTO) {
        return null;
    }
}

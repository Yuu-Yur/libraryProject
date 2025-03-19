package com.yuryuu.libraryproject.service.book;

import com.yuryuu.libraryproject.domain.*;
import com.yuryuu.libraryproject.dto.PageRequestDTO;
import com.yuryuu.libraryproject.dto.PageResponseDTO;
import com.yuryuu.libraryproject.dto.book.BookDTO;
import com.yuryuu.libraryproject.dto.book.BookStringDTO;
import com.yuryuu.libraryproject.repository.author.AuthorRepository;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import com.yuryuu.libraryproject.repository.member.MemberRepository;
import com.yuryuu.libraryproject.repository.publisher.PublisherRepository;
import com.yuryuu.libraryproject.repository.reservation.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

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

    @Override
    public String addBook(BookStringDTO bookStringDTO) throws EntityNotFoundException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Set<String> existingAuthorNames = new HashSet<>();
        authorRepository.findAll().forEach(author -> existingAuthorNames.add(author.getAuthorName()));

        // publisher 조회
        Set<String> existingPublisherNames = new HashSet<>();
        publisherRepository.findAll().forEach(pub -> existingPublisherNames.add(pub.getPublisherName()));
            // CSV의 각 행을 Book 객체로 매핑
            String title = bookStringDTO.getTitle();
            String authorsString = bookStringDTO.getAuthors();
            String publisher = bookStringDTO.getPublisher();
            Date releaseDate = Date.valueOf(LocalDate.parse(bookStringDTO.getReleaseDate() + "-03-01", formatter));
            String isbn = bookStringDTO.getIsbn();
            String kdc = bookStringDTO.getKdc();

            // 가장 먼저, 책이 이미 있으면 넘겨야함
            Optional<Book> optionalBook = bookRepository.findByTitle(title);
            if (optionalBook.isPresent()) {
                return "already exists";
            }
            Book b = optionalBook.orElseGet(() -> bookRepository.save(Book.builder()
                    .title(title)
                    .releaseDate(releaseDate)
                    .kdc(kdc)
                    .isbn(isbn)
                    .authors(new HashSet<>())
                    .build()));

            // author 리스트 확인해서 있으면 그대로 가져오고 없으면 추가한 뒤 가져옴
            String[] authors_array = authorsString.split(";");
            Set<String> authors_set = new HashSet<>(Arrays.asList(authors_array));
            Set<Author> authors = new HashSet<>();
            for (String authorName : authors_set) {
                if (!existingAuthorNames.contains(authorName)) {
                    Author newAuthor = authorRepository.save(Author.builder().authorName(authorName).books(new HashSet<>()).build());
                    authors.add(newAuthor);
                    existingAuthorNames.add(authorName);
                } else {
                    Author existingAuthor = authorRepository.findByAuthorName(authorName).get();
                    authors.add(existingAuthor);
                }
            }

            // publisher 도 마찬가지 있으면 가져오고 없으면 추가하고 가져옴
            Publisher p = null;
            if (!existingPublisherNames.contains(publisher)) {
                p = publisherRepository.save(Publisher.builder().publisherName(publisher).books(new HashSet<>()).build());
                existingPublisherNames.add(publisher);
            } else {
                p = publisherRepository.findByPublisherName(publisher).get();
            }


            // 관계 설정
            b.changePublisher(p);
            b.getAuthors().addAll(authors);
            p.getBooks().add(b);
            authors.forEach(author -> author.getBooks().add(b));
            return b.getTitle();
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
        Pageable pageable = pageRequestDTO.getPageable();
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
        String[] types = pageRequestDTO.getTypes();
        String q = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable();
        Page<Book> result = bookRepository.search(types, q, pageRequestDTO.getRating(), pageRequestDTO.getStartDate(), pageRequestDTO.getEndDate(), pageRequestDTO.getKdc(), pageRequestDTO.getIsbn(), pageable);
        List<BookDTO> dtoList = result.getContent().stream().map(this::convertToBookDTO).toList();
        return PageResponseDTO.<BookDTO>builder()
                .total((int)result.getTotalElements())
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}

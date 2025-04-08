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
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

//
@Service
@RequiredArgsConstructor
@Log4j2
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    private Book convertToBook(BookDTO bookDTO) {
        // 사실상 편집용
            Set<Author> authors = new HashSet<>(authorRepository.findAllById(bookDTO.getAuthorNos()));
            if (authors.size() != bookDTO.getAuthorNos().size()) {
                throw new EntityNotFoundException("Some authors not found");
            }
            Publisher publisher = publisherRepository.findById(bookDTO.getPublisherNo()).orElseThrow(() -> new EntityNotFoundException("Publisher not found"));

            Member member = null;
            Reservation reservation = null;
            if (bookDTO.getMemberNo() != null) {
                member = memberRepository.findById(bookDTO.getMemberNo()).orElseThrow(() -> new EntityNotFoundException("Member not found"));
            }
            if (bookDTO.getReservationNo() != null) {
                reservation = reservationRepository.findById(bookDTO.getReservationNo()).orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
            }
            return Book.builder()
                    .bookNo(bookDTO.getBookNo())
                    .title(bookDTO.getTitle())
                    .avgRating(bookDTO.getAvrRating())
                    .releaseDate(bookDTO.getReleaseDate())
                    .kdc(bookDTO.getKdc())
                    .isbn(bookDTO.getIsbn())
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
                .avrRating(book.getAvgRating())
                .releaseDate(book.getReleaseDate())
                .kdc(book.getKdc())
                .isbn(book.getIsbn())
                .returnDate(book.getReturnDate())
                .authorNos(book.getAuthors().stream().map(Author::getAuthorNo).collect(Collectors.toSet()))
                .publisherNo(book.getPublisher().getPublisherNo())
                .memberNo(book.getMember() == null ? null : book.getMember().getMemberNo())
                .reservationNo(book.getReservation() == null? null : book.getReservation().getReservationNo())
                .build();
    }

    @Override
    @Transactional
    public Boolean addBook(BookStringDTO bookStringDTO) throws EntityNotFoundException {
        if (bookStringDTO.getBookNo() != null) return false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Set<String> existingAuthorNames = new HashSet<>();
        authorRepository.findAll().forEach(author -> existingAuthorNames.add(author.getAuthorName()));

        Set<String> existingPublisherNames = new HashSet<>();
        publisherRepository.findAll().forEach(pub -> existingPublisherNames.add(pub.getPublisherName()));

            String title = bookStringDTO.getTitle();
            String authorsString = bookStringDTO.getAuthors();
            String publisher = bookStringDTO.getPublisher();
            Date releaseDate = Date.valueOf(LocalDate.parse(bookStringDTO.getReleaseDate() + "-03-01", formatter));
            String isbn = bookStringDTO.getIsbn();
            String kdc = bookStringDTO.getKdc();

            // 가장 먼저, 책이 이미 있으면 넘겨야함
            Optional<Book> optionalBook = bookRepository.findByTitle(title);
            if (optionalBook.isPresent()) {
                return false;
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
            return true;
    }

    @Override
    @Transactional
    public Boolean updateBook(BookDTO bookDTO) throws EntityNotFoundException {
        if (bookDTO.getBookNo() == null) return false;
        Book book = convertToBook(bookDTO);
        Book result = bookRepository.save(book);
        return result.getBookNo() != null;
    }

    @Override
    public void deleteBook(Long bookNo) {
        bookRepository.deleteById(bookNo);
    }

    @Override
    @Transactional
    public BookDTO getBook(Long bookNo) {
        Book book = bookRepository.findById(bookNo).orElseThrow(() ->new EntityNotFoundException("Book not found"));
        return convertToBookDTO(book);
    }

    @Override
    @Transactional
    public PageResponseDTO<BookDTO> getNewBook(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable();
        Page<Book> result = bookRepository.newest(pageable);
        List<BookDTO> dtoList = result.getContent().stream().map(this::convertToBookDTO).toList();
        return PageResponseDTO.<BookDTO>builder()
                .total(result.getTotalElements())
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    @Transactional
    public PageResponseDTO<BookDTO> searchBooks(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String q = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable();
        int rating = pageRequestDTO.getRating() == null ? 0 : pageRequestDTO.getRating();
        Date sDate = pageRequestDTO.getStartDate() == null ? null : pageRequestDTO.getStartDate();
        Date eDate = pageRequestDTO.getEndDate() == null ? null : pageRequestDTO.getEndDate();
        String kdc = pageRequestDTO.getKdc() == null ? null : pageRequestDTO.getKdc();
        String isbn = pageRequestDTO.getIsbn() == null ? null : pageRequestDTO.getIsbn();
        Page<Book> result = bookRepository.search(types, q, rating, sDate, eDate, kdc, isbn, pageable);
        List<BookDTO> dtoList = result.getContent().stream().map(this::convertToBookDTO).toList();
        return PageResponseDTO.<BookDTO>builder()
                .total(result.getTotalElements())
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    @Transactional
    public Boolean borrowBook(Long bookNo, Long memberNo) {
        Book book = bookRepository.findById(bookNo).orElseThrow(() ->new EntityNotFoundException("Book not found"));
        Member member = memberRepository.findById(memberNo).orElseThrow(() ->new EntityNotFoundException("Member not found"));
        if (book.getReturnDate() != null) return false;
        if (book.getReservation() != null) {
            if (book.getReservation().getMember().getMemberNo().equals(memberNo)) {
                book.resetBookReservation();
                book.borrowBook(member, new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));
                bookRepository.save(book);
                return true;
            }
            return false;
        }
        book.borrowBook(member, new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));
        bookRepository.save(book);
        return true;
    }

    @Override
    @Transactional
    public Boolean returnBook(Long bookNo, Long memberNo) {
        Book book = bookRepository.findById(bookNo).orElseThrow(() ->new EntityNotFoundException("Book not found"));
        Member member = memberRepository.findById(memberNo).orElseThrow(() ->new EntityNotFoundException("Member not found"));
        long diff = new Date(System.currentTimeMillis()).getTime() - book.getReturnDate().getTime();
        if (diff > 0) {
            return false;
        }
        book.returnBook();
        return true;
    }
}

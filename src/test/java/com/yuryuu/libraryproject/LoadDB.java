package com.yuryuu.libraryproject;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.yuryuu.libraryproject.domain.Author;
import com.yuryuu.libraryproject.domain.Book;
import com.yuryuu.libraryproject.domain.Publisher;
import com.yuryuu.libraryproject.dto.book.BookStringDTO;
import com.yuryuu.libraryproject.repository.author.AuthorRepository;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import com.yuryuu.libraryproject.repository.publisher.PublisherRepository;
import com.yuryuu.libraryproject.service.book.BookService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Log4j2
@SpringBootTest
public class LoadDB {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private BookService bookService;



    @Transactional
    void contextLoads() throws FileNotFoundException {
        // CSV 파일 경로 설정
        String filePath = "./src/test/java/com/yuryuu/libraryproject/도서관_DB_66만권.csv";

        // date 파서
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // CSV 파일 읽기
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] values;
            csvReader.readNext();  // 첫 번째 행 (헤더) 건너뛰기

            // author 조회
            Set<String> existingAuthorNames = new HashSet<>();
            authorRepository.findAll().forEach(author -> existingAuthorNames.add(author.getAuthorName()));

            // publisher 조회
            Set<String> existingPublisherNames = new HashSet<>();
            publisherRepository.findAll().forEach(pub -> existingPublisherNames.add(pub.getPublisherName()));
            while ((values = csvReader.readNext()) != null) {
                    // CSV의 각 행을 Book 객체로 매핑
                    String title = values[0];
                    String authorsString = values[1];
                    String publisher = values[2];
                    Date releaseDate = Date.valueOf(LocalDate.parse(values[3] + "-03-01", formatter));
                    String isbn = values[4];
                    String kdc = values[5];

                    // 가장 먼저, 책이 이미 있으면 넘겨야함
                    Optional<Book> optionalBook = bookRepository.findByTitle(title);
                    if (optionalBook.isPresent()) {
                        continue;
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
            }
        } catch (IOException e) {
            log.info("입출력 오류: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            log.info("CSV 처리 오류: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void addTest() {
        BookStringDTO bsDTO = BookStringDTO.builder()
                .title("테스트")
                .releaseDate("2025")
                .kdc("811.3")
                .isbn("995123180483")
                .authors("작가1;작가2;작가3;작가4")
                .publisher("테스트출판사")
                .build();
        String result = bookService.addBook(bsDTO);
        log.info(result);
    }
}

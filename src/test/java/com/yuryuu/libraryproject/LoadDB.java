package com.yuryuu.libraryproject;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
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
import java.time.format.DateTimeFormatter;

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



    @Test
    void contextLoads() throws FileNotFoundException {
        // CSV 파일 경로 설정
        String filePath = "./src/test/java/com/yuryuu/libraryproject/도서관_DB_66만권.csv";

        // date 파서
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // CSV 파일 읽기
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] values;
            csvReader.readNext();  // 첫 번째 행 (헤더) 건너뛰기
            int counter = 0;
            while ((values = csvReader.readNext()) != null) {
                    counter++;
                    if (counter>5) break;
                    // CSV의 각 행을 Book 객체로 매핑
                    BookStringDTO bookStringDTO = BookStringDTO.builder()
                            .title(values[0])
                            .authors(values[1])
                            .publisher(values[2])
                            .releaseDate(values[3])
                            .isbn(values[4])
                            .kdc(values[5])
                            .build();
                    bookService.addBook(bookStringDTO);
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
        bookService.addBook(bsDTO);
    }
}

package com.yuryuu.libraryproject;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.yuryuu.libraryproject.domain.Author;
import com.yuryuu.libraryproject.domain.Book;
import com.yuryuu.libraryproject.domain.Publisher;
import com.yuryuu.libraryproject.repository.author.AuthorRepository;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import com.yuryuu.libraryproject.repository.publisher.PublisherRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Log4j2
@SpringBootTest
public class LoadDB {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherRepository publisherRepository;



    @Test
    void contextLoads() throws FileNotFoundException {
        // CSV 파일 경로 설정
        String filePath = "./src/test/java/com/yuryuu/libraryproject/도서관_DB_66만권.csv";

        // 객체 리스트 초기화
        List<Book> books = new ArrayList<>();

        // CSV 파일 읽기
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] values;
            csvReader.readNext();  // 첫 번째 행 (헤더) 건너뛰기

            while ((values = csvReader.readNext()) != null) {
                // CSV의 각 행을 Book 객체로 매핑
                String title = values[0];
                String authors = values[1];
                String publisher = values[2];
                String releaseDate = values[3];
                String isbn = values[4];
                String kdc = values[5];

                // 객체 생성 authors , publisher , book
                String[] authors_array = authors.split(";");
                Set<String> authors_set = new HashSet<>(Arrays.asList(authors_array));
                // 일단 확인 없으면 만듬, 이제 책을 추가해야함 나중에
                for (String author : authors_set) {
                    Author a = authorRepository.findByAuthorName(author);
                    if (a == null) {
                        authorRepository.save(Author.builder().authorName(author).build());
                    }
                }

                Publisher p = publisherRepository.findByPublisherName(publisher);
                if (p == null) {
                    publisherRepository.save(Publisher.builder().publisherName(publisher).build());
                }
                //todo book 추가해야함

                // 리스트에 추가
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        // 결과 출력
        books.forEach(System.out::println);
    }
}

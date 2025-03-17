package com.yuryuu.libraryproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuryuu.libraryproject.dto.DbInsertBookResponseDTO;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@Log4j2
@SpringBootTest
public class LoadDB {
    @Autowired
    private BookRepository bookRepository;
//



    @Test
    void contextLoads() throws JsonProcessingException {
    String url = "https://www.nl.go.kr/seoji/SearchApi.do?cert_key=609bf2a265079b9a39a14a0166cc84e2401fbf7667020cc728adecf7dbc3&result_style=json&page_no=1&page_size=10&start_publish_date=20220509&end_publish_date=20220509";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);

        int page = jsonNode.get("PAGE_NO").asInt();
        int total = jsonNode.get("TOTAL_COUNT").asInt();
        log.info("qq" + page);

        DbInsertBookResponseDTO bookResponse = objectMapper.readValue(response, DbInsertBookResponseDTO.class);

        log.info("qq" + bookResponse.getTotalCount());


    }
}

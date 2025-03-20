package com.yuryuu.libraryproject.service.publisher;

import com.yuryuu.libraryproject.dto.publisher.PublisherDTO;

public interface PublisherService {
    Boolean addPublisher(PublisherDTO publisherDTO);
    Boolean updatePublisher(PublisherDTO publisherDTO);
    void deletePublisher(Long publisherNo);
    PublisherDTO getPublisher(Long publisherNo);
    void addBookToPublisher(Long bookNo, Long publisherNo);
}

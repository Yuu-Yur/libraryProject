package com.yuryuu.libraryproject.service.publisher;

import com.yuryuu.libraryproject.domain.Book;
import com.yuryuu.libraryproject.domain.Publisher;
import com.yuryuu.libraryproject.dto.publisher.PublisherDTO;
import com.yuryuu.libraryproject.repository.book.BookRepository;
import com.yuryuu.libraryproject.repository.publisher.PublisherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    private Publisher convertToPublisher(PublisherDTO publisherDTO) {
        Set<Book> books = null;
        if (publisherDTO.getPublisherNo() == null) {
            String publisherName = publisherDTO.getPublisherName();
            String[] types = {"p"};
            Page<Book> result = bookRepository.search(types, publisherName, 0, null, null, null, null, null);
            books = new HashSet<>(result.getContent());
        } else {
            books = new HashSet<>(bookRepository.findAllById(publisherDTO.getBookNos()));
        }
        return Publisher.builder()
                .publisherNo(publisherDTO.getPublisherNo())
                .publisherName(publisherDTO.getPublisherName())
                .books(books)
                .build();
    }
    private PublisherDTO convertToPublisherDTO(Publisher publisher) {
        return PublisherDTO.builder()
                .publisherNo(publisher.getPublisherNo())
                .publisherName(publisher.getPublisherName())
                .bookNos(publisher.getBooks().stream().map(Book::getBookNo).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public Boolean addPublisher(PublisherDTO publisherDTO) {
        if (publisherDTO.getPublisherNo() != null) return false;
        Publisher result = publisherRepository.save(convertToPublisher(publisherDTO));
        return result.getPublisherName().equals(publisherDTO.getPublisherName());
    }

    @Override
    public Boolean updatePublisher(PublisherDTO publisherDTO) {
        if (publisherDTO.getPublisherNo() == null) return false;
        Publisher result = publisherRepository.save(convertToPublisher(publisherDTO));
        return result.getPublisherName().equals(publisherDTO.getPublisherName());
    }

    @Override
    public void deletePublisher(Long publisherNo) {
        publisherRepository.deleteById(publisherNo);
    }

    @Override
    public PublisherDTO getPublisher(Long publisherNo) {
        Publisher publisher = publisherRepository.findById(publisherNo).orElseThrow(()-> new EntityNotFoundException("publisher not found"));
        return convertToPublisherDTO(publisher);
    }
}

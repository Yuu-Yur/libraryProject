package com.yuryuu.libraryproject.repository.publisher;

import com.yuryuu.libraryproject.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long>, PublisherSearch {
    @Query(value = "SELECT * FROM publisher WHERE publisher_name = ? LIMIT 1",nativeQuery = true)
    Optional<Publisher> findByPublisherName(String publisherName);
}

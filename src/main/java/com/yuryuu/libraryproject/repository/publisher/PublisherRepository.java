package com.yuryuu.libraryproject.repository.publisher;

import com.yuryuu.libraryproject.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PublisherRepository extends JpaRepository<Publisher, Long>, PublisherSearch {
    @Query(value = "SELECT * FROM publisher WHERE publisher_name = ? LIMIT 1",nativeQuery = true)
    public Publisher findByPublisherName(String publisherName);
}

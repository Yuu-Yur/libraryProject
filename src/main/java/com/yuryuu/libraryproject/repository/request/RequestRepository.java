package com.yuryuu.libraryproject.repository.request;

import com.yuryuu.libraryproject.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query(value = "SELECT * FROM request WHERE title = ? AND authors = ?", nativeQuery = true)
    Optional<Request> findByTA(String title, String authors);
}

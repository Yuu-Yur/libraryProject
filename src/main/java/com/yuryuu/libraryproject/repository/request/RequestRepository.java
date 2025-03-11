package com.yuryuu.libraryproject.repository.request;

import com.yuryuu.libraryproject.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}

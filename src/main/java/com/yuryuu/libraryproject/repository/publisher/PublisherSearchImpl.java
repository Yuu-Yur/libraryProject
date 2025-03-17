package com.yuryuu.libraryproject.repository.publisher;

import com.yuryuu.libraryproject.domain.Publisher;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class PublisherSearchImpl extends QuerydslRepositorySupport implements PublisherSearch {
    public PublisherSearchImpl() {super(Publisher.class);}
}

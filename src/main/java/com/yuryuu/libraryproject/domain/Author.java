package com.yuryuu.libraryproject.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorNo;

    private String authorName;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Book> books;
}

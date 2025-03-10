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
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publisherNo;

    private String publisherName;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Book> books;
}

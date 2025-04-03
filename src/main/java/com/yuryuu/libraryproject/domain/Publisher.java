package com.yuryuu.libraryproject.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String publisherName;
    @OneToMany(mappedBy = "publisher",fetch = FetchType.LAZY)
    private Set<Book> books;

    public void changeBooks(Set<Book> books) {
        this.books = books;
    }
}

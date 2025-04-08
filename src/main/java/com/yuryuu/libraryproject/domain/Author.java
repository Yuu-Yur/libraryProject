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
@Setter
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorNo;

    @NotBlank
    private String authorName;
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private Set<Book> books;


}

package com.yuryuu.libraryproject.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Date;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestNo;

    @NotBlank
    private String title;
    @NotBlank
    private String authors;
    private String publisher;
    private Date releaseDate;

    @ManyToMany(mappedBy = "requests", fetch = FetchType.LAZY)
    private Set<Member> members;
}

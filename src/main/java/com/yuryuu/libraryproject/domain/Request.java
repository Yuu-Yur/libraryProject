package com.yuryuu.libraryproject.domain;

import jakarta.persistence.*;
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
    private Integer requestNo;

    private String title;
    private String authors;
    private String publisher;
    private Date releaseDate;
    private Boolean accepted;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Member> member;
}

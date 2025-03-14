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
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookNo;

    private String title;
    private String description;
    private Float avrRating;
    private Date releaseDate;
    private String kdc;

    private Date returnDate;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Author> authors;
    @ManyToOne(fetch = FetchType.LAZY)
    private Publisher publisher;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;
}

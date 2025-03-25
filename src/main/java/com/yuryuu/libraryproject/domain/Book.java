package com.yuryuu.libraryproject.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Column(unique = true)
    @NotBlank
    private String title;
    @NotNull
    private Date releaseDate;
    @NotBlank
    private String kdc;
    @NotBlank
    private String isbn;

    private Float avgRating;

    private Date returnDate;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Author> authors;
    @ManyToOne(fetch = FetchType.LAZY)
    private Publisher publisher;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    private Reservation reservation;
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Review> reviews;

    public void changeAuthors(Set<Author> authors) {
        this.authors = authors;
    }
    public void changePublisher(Publisher publisher) {
        this.publisher = publisher;
    }
    public void borrowBook(Member member, Date returnDate) {
        this.returnDate = returnDate;
        this.member = member;
    }
    public void returnBook() {
        this.returnDate = null;
        this.member = null;
    }
    public void reserveBook(Reservation reservation) {
        this.reservation = reservation;
    }
    public void resetBookReservation() {
        this.reservation = null;
    }
    public void changeAvgRating(Float avgRating) {
        this.avgRating = avgRating;
    }
}

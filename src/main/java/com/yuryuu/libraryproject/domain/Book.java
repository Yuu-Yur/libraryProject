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

    @Column(nullable = false, unique = true)
    private String title;
    private Date releaseDate;
    private String kdc;
    private String isbn;

    private Float avrRating;

    private Date returnDate;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Author> authors;
    @ManyToOne(fetch = FetchType.LAZY)
    private Publisher publisher;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

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
}

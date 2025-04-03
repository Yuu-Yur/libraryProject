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
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;
    private String email;
    private String password;
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private Set<Reservation> reservations;
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Review> reviews;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(joinColumns = @JoinColumn(name = "member_no"),
                inverseJoinColumns = @JoinColumn(name = "request_no"))
    private Set<Request> requests;

    public void reserveBook(Reservation reservation) {
        this.reservations.add(reservation);
    }
}

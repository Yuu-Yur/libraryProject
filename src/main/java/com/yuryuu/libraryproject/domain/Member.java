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
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Reservation> reservations;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Review> reviews;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Request> requests;
}

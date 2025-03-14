package com.yuryuu.libraryproject.dto.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Date;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDTO {
    // 기본 책 정보들
    private Long bookNo;

    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @Builder.Default
    private Float avrRating = 0.0f;
    @NotEmpty
    private Date releaseDate;
    @NotEmpty
    private String kdc;

    private Date returnDate;
    // 작가 필요
    @NotNull
    private Set<Long> authorNos;
    // 출판사 필요
    @NotNull
    private Long publisherNo;
    // 멤버 필수아님
    private Long memberNo;
    // 예약번호도 필수 아님
    private Long reservationNo;
}

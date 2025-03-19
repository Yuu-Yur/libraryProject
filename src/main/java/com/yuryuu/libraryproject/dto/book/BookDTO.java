package com.yuryuu.libraryproject.dto.book;

import jakarta.validation.constraints.NotBlank;
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
    // ru용 DTO
    private Long bookNo;

    @NotBlank
    private String title;
    @Builder.Default
    private Float avrRating = 0.0f;
    @NotBlank
    private Date releaseDate;
    @NotBlank
    private String kdc;
    @NotBlank
    private String isbn;

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

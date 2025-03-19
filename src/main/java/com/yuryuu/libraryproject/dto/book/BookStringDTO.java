package com.yuryuu.libraryproject.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookStringDTO {
    // c 용 DTO
    private Long bookNo;

    @NotBlank
    private String title;
    @Pattern(regexp = "^[0-9]{4}$", message = "Release date must be a 4-digit number")
    private String releaseDate;

    private String kdc;

    private String isbn;

    @Builder.Default
    private Float avrRating = 0.0f;

    @NotBlank
    private String authors;
    @NotBlank
    private String publisher;
}

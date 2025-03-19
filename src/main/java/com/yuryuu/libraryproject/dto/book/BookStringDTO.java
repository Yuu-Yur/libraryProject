package com.yuryuu.libraryproject.dto.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookStringDTO {
    // crud 용 DTO
    private Long bookNo;

    @NotEmpty
    private String title;
    @Builder.Default
    private Float avrRating = 0.0f;
    @NotEmpty
    @Pattern(regexp = "^[0-9]{4}$", message = "Release date must be a 4-digit number")
    private String releaseDate;
    @NotEmpty
    private String kdc;
    @NotEmpty
    private String isbn;


    @NotNull
    private String authors;

    @NotNull
    private String publisher;
}

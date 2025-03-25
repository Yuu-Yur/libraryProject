package com.yuryuu.libraryproject.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewDTO {
    private Long reviewNo;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
    @Builder.Default
    private Integer good = 0;
    @Builder.Default
    private Integer bad = 0;
    @NotBlank
    private String content;
    @NotNull
    private Long bookNo;
    @NotNull
    private Long memberNo;
}

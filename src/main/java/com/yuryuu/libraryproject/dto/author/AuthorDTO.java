package com.yuryuu.libraryproject.dto.author;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AuthorDTO {

    private Long authorNo;

    @NotBlank
    private String authorName;

    private Set<Long> bookNos;
}

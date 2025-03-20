package com.yuryuu.libraryproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Date;
import java.util.Set;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDTO {
    private Long requestNo;
    @NotBlank
    private String title;
    @NotBlank
    private String authors;
    private String publisher;
    private Date releaseDate;

    private Set<Long> memberNos;
}

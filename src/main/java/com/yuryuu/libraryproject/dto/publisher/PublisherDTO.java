package com.yuryuu.libraryproject.dto.publisher;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PublisherDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publisherNo;

    @NotBlank
    private String publisherName;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Long> bookNos;
}

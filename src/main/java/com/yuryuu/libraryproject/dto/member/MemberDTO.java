package com.yuryuu.libraryproject.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {
    private Long memberNo;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    private Set<Long> reservationNos;
    private Set<Long> reviewNos;
    private Set<Long> requestNos;
}

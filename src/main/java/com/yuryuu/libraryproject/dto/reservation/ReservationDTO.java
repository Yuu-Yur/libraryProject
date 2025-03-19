package com.yuryuu.libraryproject.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationDTO {
    private Long reservationNo;
    @NotBlank
    private Long bookNo;
    @NotBlank
    private Long memberNo;
}

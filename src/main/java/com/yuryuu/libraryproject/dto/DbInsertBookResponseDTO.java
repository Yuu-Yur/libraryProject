package com.yuryuu.libraryproject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DbInsertBookResponseDTO {
    @JsonProperty("TOTAL_COUNT")
    private String totalCount;

    @JsonProperty("PAGE_NO")
    private String pageNo;

    @JsonProperty("docs")
    private List<DbInsertBookDTO> docs;

}

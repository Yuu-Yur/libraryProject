package com.yuryuu.libraryproject.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;
    private String type;
    private String keyword;
    private String link;
    private Integer rating;
    private Date startDate;
    private Date endDate;
    private String kdc;
    private String isbn;

    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        //
        return type.split("");
    }
    // ...props 가변 parameter 여러개의 parameter 을 받을 수 있음
    public Pageable getPageable(String ...props) {
        Pageable pageable = PageRequest.of(this.page - 1, this.size
        , Sort.by(props).descending());
        return pageable;
    }

    public String getLink(){
        if (link == null || link.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            // 페이징 정보 page , size
            builder.append("page=").append(this.page).append("&size=").append(this.size);
            if (type != null && !type.isEmpty()) {
                // 검색 정보 type
                builder.append("&type=").append(type);
            }
            if (keyword != null && !keyword.isEmpty()) {
                try {
                    builder.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rating != null && !(rating == 0)) {
                builder.append("&rating=").append(rating);
            }
            if (startDate != null) {
                builder.append("&sDate=").append(startDate);
            }
            if (endDate != null) {
                builder.append("&eDate=").append(endDate);
            }
            if (kdc != null && !kdc.isEmpty()) {
                builder.append("&kdc=").append(kdc);
            }
            if (isbn != null && !isbn.isEmpty()) {
                builder.append("&isbn=").append(isbn);
            }
            link = builder.toString();
        }
        return link;
    }
}

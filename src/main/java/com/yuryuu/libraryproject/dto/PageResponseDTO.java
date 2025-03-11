package com.yuryuu.libraryproject.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PageResponseDTO<E> {
    private int total;
    private int page;
    private int size;
    private int start; // 한 화면에 보일 첫 페이지
    private int end; // 한 화면에 보일 마지막 페이지
    private boolean prev;
    private boolean next;
    private int last; // 마지막 페이지 넘버
    private List<E> dtoList;
    private int pageSize;

    @Builder
    public PageResponseDTO(List<E> dtoList, int total, PageRequestDTO pageRequestDTO) {
        this.total = total;
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.dtoList = dtoList;
        this.last = (int)Math.ceil((double) total /size);
        this.end = (int)Math.ceil((double) page /pageSize)*pageSize;
        this.end = Math.min(end, last);
        this.start = end - pageSize + 1;

        // 위는 1~pageSize 고정의 경우
        // 현재 페이지를 기준으로 위아래로 3~4개정도를 표시하려면?
        this.end = page + 3;
        this.end = Math.min(end, last);
        this.start = page - 3;
        this.start = Math.max(start, 1);

        this.next = last > end; //즉 last 가 end 보다 크면
        this.prev = start > 1; // start 이전에 최소한 1이라도 있으므로
    }
}

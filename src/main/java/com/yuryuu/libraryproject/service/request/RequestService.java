package com.yuryuu.libraryproject.service.request;
import com.yuryuu.libraryproject.dto.request.RequestDTO;

public interface RequestService {
    Boolean addRequest(RequestDTO requestDTO);
    Boolean updateRequest(RequestDTO requestDTO);
    void deleteRequest(Long requestNo);
    RequestDTO getRequest(Long requestNo);
}

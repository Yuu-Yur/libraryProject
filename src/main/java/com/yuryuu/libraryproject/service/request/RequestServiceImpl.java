package com.yuryuu.libraryproject.service.request;

import com.yuryuu.libraryproject.domain.Member;
import com.yuryuu.libraryproject.domain.Request;
import com.yuryuu.libraryproject.dto.request.RequestDTO;
import com.yuryuu.libraryproject.repository.member.MemberRepository;
import com.yuryuu.libraryproject.repository.request.RequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final MemberRepository memberRepository;

    private Request convertToRequest(RequestDTO requestDTO) {
        Set<Member> members = new HashSet<>(memberRepository.findAllById(requestDTO.getMemberNos()));
        return Request.builder()
                .requestNo(requestDTO.getRequestNo())
                .title(requestDTO.getTitle())
                .authors(requestDTO.getAuthors())
                .publisher(requestDTO.getPublisher())
                .releaseDate(requestDTO.getReleaseDate())
                .members(members)
                .build();
    }
    private RequestDTO convertToRequestDTO(Request request) {
        return RequestDTO.builder()
                .requestNo(request.getRequestNo())
                .title(request.getTitle())
                .authors(request.getAuthors())
                .publisher(request.getPublisher())
                .releaseDate(request.getReleaseDate())
                .memberNos(request.getMembers().stream().map(Member::getMemberNo).collect(Collectors.toSet())).build();
    }

    @Override
    public Boolean addRequest(RequestDTO requestDTO) {
        if (requestDTO.getRequestNo() != null) return false;
        Optional<Request> existRequest = requestRepository.findByTA(requestDTO.getTitle(), requestDTO.getAuthors());
        if (existRequest.isPresent()) {
            Request request = existRequest.get();
            Set<Member> members = new HashSet<>(memberRepository.findAllById(requestDTO.getMemberNos()));
            request.getMembers().addAll(members);
            requestRepository.save(request);
        } else {
            requestRepository.save(convertToRequest(requestDTO));
        }
        return true;
    }

    @Override
    public Boolean updateRequest(RequestDTO requestDTO) {
        if (requestDTO.getRequestNo() == null) return false;
        requestRepository.save(convertToRequest(requestDTO));
        return true;
    }

    @Override
    public void deleteRequest(Long requestNo) {
        requestRepository.deleteById(requestNo);
    }

    @Override
    public RequestDTO getRequest(Long requestNo) {
        Request request = requestRepository.findById(requestNo).orElseThrow(() -> new EntityNotFoundException("Request not found"));
        return convertToRequestDTO(request);
    }
}

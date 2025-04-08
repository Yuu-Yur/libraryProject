package com.yuryuu.libraryproject.controller.request;

import com.yuryuu.libraryproject.dto.request.RequestDTO;
import com.yuryuu.libraryproject.service.request.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
@Log4j2
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/add")
    public Boolean addRequest(@Valid @RequestBody RequestDTO requestDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return requestService.addRequest(requestDTO);
    }

    @PutMapping("/update")
    public Boolean updateRequest(@Valid @RequestBody RequestDTO requestDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return requestService.updateRequest(requestDTO);
    }

    @DeleteMapping("/delete/{RequestNo}")
    public void deleteRequest(@PathVariable("RequestNo") Long requestNo) {
        requestService.deleteRequest(requestNo);
    }

    @GetMapping("/{RequestNo}/")
    public RequestDTO getRequest(@PathVariable("RequestNo") Long requestNo) {
        return requestService.getRequest(requestNo);
    }
}

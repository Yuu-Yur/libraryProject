package com.yuryuu.libraryproject.controller.publisher;

import com.yuryuu.libraryproject.dto.publisher.PublisherDTO;
import com.yuryuu.libraryproject.service.publisher.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publisher")
@RequiredArgsConstructor
@Log4j2
public class PublisherController {
    private final PublisherService publisherService;

    @PostMapping("/add")
    public Boolean addPublisher(@Valid @RequestBody PublisherDTO publisherDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return publisherService.addPublisher(publisherDTO);
    }

    @PutMapping("/update")
    public Boolean updatePublisher(@Valid @RequestBody PublisherDTO publisherDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return publisherService.updatePublisher(publisherDTO);
    }

    @DeleteMapping("/delete/{PublisherNo}")
    public void deletePublisher(@PathVariable("PublisherNo") Long publisherNo) {
        publisherService.deletePublisher(publisherNo);
    }

    @GetMapping("/{PublisherNo}/")
    public PublisherDTO getPublisher(@PathVariable("PublisherNo") Long publisherNo) {
        return publisherService.getPublisher(publisherNo);
    }
}

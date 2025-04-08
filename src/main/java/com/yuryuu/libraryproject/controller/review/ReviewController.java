package com.yuryuu.libraryproject.controller.review;

import com.yuryuu.libraryproject.dto.review.ReviewDTO;
import com.yuryuu.libraryproject.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Log4j2
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/add")
    public Boolean addReview(@Valid @RequestBody ReviewDTO reviewDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return reviewService.addReview(reviewDTO);
    }

    @PutMapping("/update")
    public Boolean updateReview(@Valid @RequestBody ReviewDTO reviewDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return reviewService.updateReview(reviewDTO);
    }

    @DeleteMapping("/delete/{ReviewNo}")
    public void deleteReview(@PathVariable("ReviewNo") Long reviewNo) {
        reviewService.deleteReview(reviewNo);
    }

    @GetMapping("/{ReviewNo}/")
    public ReviewDTO getReview(@PathVariable("ReviewNo") Long reviewNo) {
        return reviewService.getReview(reviewNo);
    }
}

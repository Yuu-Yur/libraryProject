package com.yuryuu.libraryproject.controller.author;

import com.yuryuu.libraryproject.dto.PageRequestDTO;
import com.yuryuu.libraryproject.dto.PageResponseDTO;
import com.yuryuu.libraryproject.dto.author.AuthorDTO;
import com.yuryuu.libraryproject.dto.member.MemberDTO;
import com.yuryuu.libraryproject.service.author.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
@Log4j2
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping("/add")
    @Transactional
    public AuthorDTO addAuthor(@Valid @RequestBody AuthorDTO authorDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return authorService.addAuthor(authorDTO);
    }

    @PutMapping("/update")
    public Boolean updateAuthor(@Valid @RequestBody AuthorDTO authorDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return authorService.updateAuthor(authorDTO);
    }

    @DeleteMapping("/delete/{authorNo}")
    public void deleteAuthor(@PathVariable("authorNo") Long authorNo) {
        authorService.deleteAuthor(authorNo);
    }

    @GetMapping("/{authorNo}")
    public AuthorDTO getAuthor(@PathVariable("authorNo") Long authorNo) {
        return authorService.getAuthor(authorNo);
    }

    @GetMapping("/list")
    public PageResponseDTO<AuthorDTO> listAuthor(PageRequestDTO pageRequestDTO) {
        return authorService.getAuthors(pageRequestDTO);
    }

    @GetMapping("/search")
    public PageResponseDTO<AuthorDTO> searchAuthor(MemberDTO memberDTO, PageRequestDTO pageRequestDTO) {
        return authorService.searchAuthors(pageRequestDTO);
    }
}

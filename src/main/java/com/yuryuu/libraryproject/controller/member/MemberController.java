package com.yuryuu.libraryproject.controller.member;

import com.yuryuu.libraryproject.dto.member.MemberDTO;
import com.yuryuu.libraryproject.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/add")
    public Boolean addMember(@Valid @RequestBody MemberDTO memberDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return memberService.addMember(memberDTO);
    }

    @PutMapping("/update")
    public Boolean updateMember(@Valid @RequestBody MemberDTO memberDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        return memberService.updateMember(memberDTO);
    }

    @DeleteMapping("/delete/{MemberNo}")
    public void deleteMember(@PathVariable("MemberNo") Long memberNo) {
        memberService.deleteMember(memberNo);
    }

    @GetMapping("/{MemberNo}/")
    public MemberDTO getMember(@PathVariable("MemberNo") Long memberNo) {
        return memberService.getMember(memberNo);
    }
}

package com.yuryuu.libraryproject.service.member;

import com.yuryuu.libraryproject.dto.member.MemberDTO;

public interface MemberService {
    Boolean addMember(MemberDTO memberDTO);
    Boolean updateMember(MemberDTO memberDTO);
    void deleteMember(Long memberNo);
    MemberDTO getMember(Long memberNo);
}

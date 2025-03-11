package com.yuryuu.libraryproject.repository.member;

import com.yuryuu.libraryproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

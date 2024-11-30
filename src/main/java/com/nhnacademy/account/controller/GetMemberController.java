package com.nhnacademy.account.controller;

import com.nhnacademy.account.entity.member.Member;
import com.nhnacademy.account.repository.MemberRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class GetMemberController {

    private final MemberRepository memberRepository;

    public GetMemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/{memberId}")
    public Member getMember(@PathVariable String memberId) {
        Member member = memberRepository.findByMemberId(memberId);

        if (member == null) {
            throw new IllegalArgumentException("Member not found with ID: " + memberId);
        }

        return member;
    }
}

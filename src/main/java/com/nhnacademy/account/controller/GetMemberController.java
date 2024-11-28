package com.nhnacademy.account.controller;

import com.nhnacademy.account.entity.member.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class GetMemberController {

    @GetMapping("/{memberId}")
    public Member getMember(@PathVariable("memberId") Member member) {
        return member;
    }

}

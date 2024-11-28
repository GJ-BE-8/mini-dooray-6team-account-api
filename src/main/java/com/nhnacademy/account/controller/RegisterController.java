package com.nhnacademy.account.controller;


import com.nhnacademy.account.entity.Member;
import com.nhnacademy.account.entity.MemberCreateRequest;
import com.nhnacademy.account.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class RegisterController {

    private final MemberService memberService;

    public RegisterController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity registerMember(@RequestBody MemberCreateRequest request) {
        Member member = new Member(request.getMemberId(), request.getPassword(), request.getEmail(), request.getName());
        memberService.register(member);
        return ResponseEntity.ok().build();
    }

}

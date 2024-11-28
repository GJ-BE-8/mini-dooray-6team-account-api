package com.nhnacademy.account.controller;

import com.nhnacademy.account.advice.exception.LoginFailException;
import com.nhnacademy.account.entity.LoginRequest;
import com.nhnacademy.account.entity.Member;
import com.nhnacademy.account.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login")
@RestController
public class LoginController {

    private final MemberService service;

    public LoginController(MemberService service) {
        this.service = service;
    }

    @PostMapping
    public Member login(@RequestBody LoginRequest request) {
        if (service.matches(request.getMemberId(), request.getPassword())) {
            return service.getByMemberId(request.getMemberId());
        }
        throw new LoginFailException(request.getMemberId()+": login fail");
    }
}

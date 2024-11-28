package com.nhnacademy.account.controller;

import com.nhnacademy.account.entity.Member;
import com.nhnacademy.account.entity.StatusRequest;
import com.nhnacademy.account.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    private final MemberService service;

    public StatusController(MemberService service) {
        this.service = service;
    }

    @PostMapping("/members/{memberId}") // 어떻게 요청을 받을지 할지 생각하기
    public Member updateStatus(@RequestBody StatusRequest request)
    {
      return service.updateStatus(request.getMemberId(),request.getStatus());
    }
}

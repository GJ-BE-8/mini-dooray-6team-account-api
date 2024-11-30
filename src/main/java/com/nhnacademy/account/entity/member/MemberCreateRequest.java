package com.nhnacademy.account.entity.member;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateRequest {

    private String memberId;
    private String password;
    private String name;
    private String email;
}

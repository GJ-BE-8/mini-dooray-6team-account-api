package com.nhnacademy.account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    private String memberId;
    private String password;
}

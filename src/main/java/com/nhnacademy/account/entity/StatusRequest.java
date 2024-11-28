package com.nhnacademy.account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusRequest {
    private String memberId;
    private Status status;
}

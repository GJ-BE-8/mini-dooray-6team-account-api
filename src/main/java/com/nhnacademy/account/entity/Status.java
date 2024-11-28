package com.nhnacademy.account.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

//순서대로 휴면, 가입, 탈퇴
public enum Status {
    DORMANT, ACTIVE, DELETED;

    @JsonCreator
    public static Status formString(String s) {
        return Status.valueOf(s.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return this.name().toLowerCase();
    }
}



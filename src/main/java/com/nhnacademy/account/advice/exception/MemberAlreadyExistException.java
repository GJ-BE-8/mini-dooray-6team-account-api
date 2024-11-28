package com.nhnacademy.account.advice.exception;

public class MemberAlreadyExistException extends RuntimeException {

    public MemberAlreadyExistException(String message) {
        super(message);
    }
}

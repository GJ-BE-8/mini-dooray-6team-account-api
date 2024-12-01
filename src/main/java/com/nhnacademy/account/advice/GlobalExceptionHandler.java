package com.nhnacademy.account.advice;

import com.nhnacademy.account.advice.exception.LoginFailException;
import com.nhnacademy.account.advice.exception.MemberAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 공통 응답 생성 메서드
    private ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatus status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MemberAlreadyExistException.class)
    public ResponseEntity<Map<String, Object>> handleMemberAlreadyExist(MemberAlreadyExistException ex) {
        return buildResponseEntity(HttpStatus.CONFLICT, "Member Already Exists", ex.getMessage());
    }

    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<Map<String, Object>> handleLoginFail(LoginFailException ex) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, "Login Failed", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // 추가적인 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }
}

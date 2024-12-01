package com.nhnacademy.account.controller;

import com.nhnacademy.account.config.SecurityConfig;
import com.nhnacademy.account.entity.member.LoginRequest;
import com.nhnacademy.account.entity.member.Member;
import com.nhnacademy.account.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@Import(SecurityConfig.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member(1L, "member1", "Test Name", "password123", "test@example.com", com.nhnacademy.account.entity.member.Status.ACTIVE);
    }

    @Test
    void loginSuccess() throws Exception {
        LoginRequest request = new LoginRequest("member1", "password123");

        // Mocking
        when(memberService.matches(request.getMemberId(), request.getPassword())).thenReturn(true);
        when(memberService.getByMemberId(request.getMemberId())).thenReturn(testMember);

        // Perform POST request
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\":\"member1\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value("member1"))
                .andExpect(jsonPath("$.name").value("Test Name"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        // Verify Service calls
        verify(memberService, times(1)).matches(request.getMemberId(), request.getPassword());
        verify(memberService, times(1)).getByMemberId(request.getMemberId());
    }

    @Test
    void loginFailPassword() throws Exception {
        LoginRequest request = new LoginRequest("member1", "wrongPassword");

        // Mocking
        when(memberService.matches(request.getMemberId(), request.getPassword())).thenReturn(false);

        // Perform POST request
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\":\"member1\", \"password\":\"wrongPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Login Failed"))
                .andExpect(jsonPath("$.message").value("member1: login fail"));

        // Verify Service calls
        verify(memberService, times(1)).matches(request.getMemberId(), request.getPassword());
        verify(memberService, never()).getByMemberId(anyString());
    }

    @Test
    void loginFailNotFoundMember() throws Exception {
        LoginRequest request = new LoginRequest("nonexistentUser", "password123");

        // Mocking
        when(memberService.matches(request.getMemberId(), request.getPassword())).thenReturn(false);

        // Perform POST request
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\":\"nonexistentUser\", \"password\":\"password123\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Login Failed"))
                .andExpect(jsonPath("$.message").value("nonexistentUser: login fail"));

        // Verify Service calls
        verify(memberService, times(1)).matches(request.getMemberId(), request.getPassword());
        verify(memberService, never()).getByMemberId(anyString());
    }
}

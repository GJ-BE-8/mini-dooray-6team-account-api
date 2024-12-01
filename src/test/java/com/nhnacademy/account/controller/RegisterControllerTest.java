package com.nhnacademy.account.controller;

import com.nhnacademy.account.advice.exception.MemberAlreadyExistException;
import com.nhnacademy.account.config.SecurityConfig;
import com.nhnacademy.account.entity.member.Member;
import com.nhnacademy.account.entity.member.MemberCreateRequest;
import com.nhnacademy.account.service.MemberService;
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

@WebMvcTest(RegisterController.class)
@Import(SecurityConfig.class)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    void register() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("member1", "password123", "test@example.com", "Test Name");

        // Perform POST request
        mockMvc.perform(post("/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\":\"member1\", \"password\":\"password123\", \"email\":\"test@example.com\", \"name\":\"Test Name\"}"))
                .andExpect(status().isOk());

        // Verify service call
        verify(memberService, times(1)).register(any(Member.class));
    }

    @Test
    void registerFail() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("member1", "password123", "test@example.com", "Test Name");

        // Mocking exception
        doThrow(new MemberAlreadyExistException("member1 already exists")).when(memberService).register(any(Member.class));

        // Perform POST request
        mockMvc.perform(post("/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\":\"member1\", \"password\":\"password123\", \"email\":\"test@example.com\", \"name\":\"Test Name\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Member Already Exists"))
                .andExpect(jsonPath("$.message").value("member1 already exists"));

        // Verify service call
        verify(memberService, times(1)).register(any(Member.class));
    }
}

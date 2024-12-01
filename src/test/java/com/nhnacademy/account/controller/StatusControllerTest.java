package com.nhnacademy.account.controller;

import com.nhnacademy.account.config.SecurityConfig;
import com.nhnacademy.account.entity.member.Member;
import com.nhnacademy.account.entity.member.Status;
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

@WebMvcTest(StatusController.class)
@Import(SecurityConfig.class)

class StatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    void updateStatus() throws Exception {
        // Mock member
        Member member = new Member(1L, "member1", "password123", "test@example.com", "Test Name", Status.ACTIVE);

        // Mock service behavior
        when(memberService.updateStatus("member1", Status.DORMANT)).thenReturn(member);

        // Perform POST request
        mockMvc.perform(post("/members/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\":\"member1\", \"status\":\"DORMANT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value("member1"))
                .andExpect(jsonPath("$.status").value("active")); // 기대값을 소문자로 수정

        // Verify service interaction
        verify(memberService, times(1)).updateStatus("member1", Status.DORMANT);
    }


    @Test
    void updateStatusFail() throws Exception {
        // Mock service to throw exception
        doThrow(new IllegalArgumentException("Member not found")).when(memberService).updateStatus("nonexistent", Status.DORMANT);

        // Perform POST request
        mockMvc.perform(post("/members/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\":\"nonexistent\", \"status\":\"DORMANT\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Member not found"));

        // Verify service interaction
        verify(memberService, times(1)).updateStatus("nonexistent", Status.DORMANT);
    }
}

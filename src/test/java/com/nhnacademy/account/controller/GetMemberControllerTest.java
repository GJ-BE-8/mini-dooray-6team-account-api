package com.nhnacademy.account.controller;

import com.nhnacademy.account.entity.member.Member;
import com.nhnacademy.account.entity.member.Status;
import com.nhnacademy.account.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GetMemberController.class)
class GetMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member(1L, "member1", "Test Name", "password123", "test@example.com", Status.ACTIVE);
        when(memberRepository.findByMemberId("member1")).thenReturn(testMember);
    }


    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void getMember_existingMember_returnsMember() throws Exception {
        when(memberRepository.findByMemberId("member1")).thenReturn(testMember);

        mockMvc.perform(get("/members/{memberId}", "member1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("active"))
                .andExpect(jsonPath("$.memberId").value("member1"))
                .andExpect(jsonPath("$.email").value("test@example.com")); // email 필드 확인

        verify(memberRepository, times(1)).findByMemberId("member1");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void getMember_nonExistingMember_returnsError() throws Exception {
        when(memberRepository.findByMemberId("nonexistent")).thenReturn(null);

        mockMvc.perform(get("/members/{memberId}", "nonexistent"))
                .andExpect(status().isBadRequest()) // Bad Request 상태 확인
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Member not found with ID: nonexistent"));

        verify(memberRepository, times(1)).findByMemberId("nonexistent");
    }

}

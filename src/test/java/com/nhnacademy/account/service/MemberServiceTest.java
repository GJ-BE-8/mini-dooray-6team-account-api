package com.nhnacademy.account.service;

import com.nhnacademy.account.advice.exception.MemberAlreadyExistException;
import com.nhnacademy.account.entity.member.Member;
import com.nhnacademy.account.entity.member.Status;
import com.nhnacademy.account.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        memberService = new MemberService(memberRepository, passwordEncoder);
    }

    @Test
    void matches() {
        // given
        String memberId = "testUser";
        String password = "password123";
        Member mockMember = new Member(memberId, "encodedPassword", "test@example.com", "Test User");

        when(memberRepository.findByMemberId(memberId)).thenReturn(mockMember);
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);

        // when
        boolean result = memberService.matches(memberId, password);

        // then
        assertThat(result).isTrue();
        verify(memberRepository).findByMemberId(memberId);
        verify(passwordEncoder).matches(password, "encodedPassword");
    }

    @Test
    void matchesFailWithPassword() {
        // given
        String memberId = "testUser";
        String password = "wrongPassword";
        Member mockMember = new Member(memberId, "encodedPassword", "test@example.com", "Test User");

        when(memberRepository.findByMemberId(memberId)).thenReturn(mockMember);
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        // when
        boolean result = memberService.matches(memberId, password);

        // then
        assertThat(result).isFalse();
        verify(memberRepository).findByMemberId(memberId);
        verify(passwordEncoder).matches(password, "encodedPassword");
    }

    @Test
    void matchesFailWithMemberId() {
        // given
        String memberId = "unknownUser";
        String password = "password123";

        when(memberRepository.findByMemberId(memberId)).thenReturn(null);

        // when
        boolean result = memberService.matches(memberId, password);

        // then
        assertThat(result).isFalse();
        verify(memberRepository).findByMemberId(memberId);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void register() {
        // given
        String memberId = "newUser";
        Member newMember = new Member(memberId, "rawPassword", "new@example.com", "New User");
        String encodedPassword = "encodedPassword";

        when(memberRepository.countByMemberId(memberId)).thenReturn(0);
        when(passwordEncoder.encode("rawPassword")).thenReturn(encodedPassword);

        // when
        memberService.register(newMember);

        // then
        assertThat(newMember.getPassword()).isEqualTo(encodedPassword);
        verify(memberRepository).countByMemberId(memberId);
        verify(passwordEncoder).encode("rawPassword");
        verify(memberRepository).save(newMember);
    }

    @Test
    void registerFail() {
        // given
        String memberId = "existingUser";
        Member existingMember = new Member(memberId, "password123", "existing@example.com", "Existing Use");

        when(memberRepository.countByMemberId(memberId)).thenReturn(1);

        // when / then
        assertThrows(MemberAlreadyExistException.class, () -> memberService.register(existingMember));
        verify(memberRepository).countByMemberId(memberId);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void getByMemberId() {
        // given
        String memberId = "testUser";
        Member mockMember = new Member(memberId, "password123", "test@example.com", "Test User");

        when(memberRepository.findByMemberId(memberId)).thenReturn(mockMember);

        // when
        Member result = memberService.getByMemberId(memberId);

        // then
        assertThat(result).isEqualTo(mockMember);
        verify(memberRepository).findByMemberId(memberId);
    }

    @Test
    void getByMemberIdFail() {
        // given
        String memberId = "unknownUser";

        when(memberRepository.findByMemberId(memberId)).thenReturn(null);

        // when
        Member result = memberService.getByMemberId(memberId);

        // then
        assertThat(result).isNull();
        verify(memberRepository).findByMemberId(memberId);
    }

    @Test
    void updateStatus() {
        // given
        String memberId = "testUser";
        Status newStatus = Status.DORMANT;
        Member mockMember = new Member(memberId, "password123", "test@example.com", "Test User");

        when(memberRepository.findByMemberId(memberId)).thenReturn(mockMember);
        when(memberRepository.save(mockMember)).thenReturn(mockMember);

        // when
        Member updatedMember = memberService.updateStatus(memberId, newStatus);

        // then
        assertThat(updatedMember.getStatus()).isEqualTo(newStatus);
        verify(memberRepository).findByMemberId(memberId);
        verify(memberRepository).save(mockMember);
    }

    @Test
    void updateStatusFail() {
        // given
        String memberId = "unknownUser";
        Status newStatus = Status.DELETED;

        when(memberRepository.findByMemberId(memberId)).thenReturn(null);

        // when / then
        assertThrows(NullPointerException.class, () -> memberService.updateStatus(memberId, newStatus));
        verify(memberRepository).findByMemberId(memberId);
    }
}

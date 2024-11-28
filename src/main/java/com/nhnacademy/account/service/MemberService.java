package com.nhnacademy.account.service;

import com.nhnacademy.account.advice.exception.MemberAlreadyExistException;
import com.nhnacademy.account.entity.Member;
import com.nhnacademy.account.entity.Status;
import com.nhnacademy.account.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder encoder;

    public MemberService(MemberRepository repository, PasswordEncoder encoder) {
        this.repository = repository;

        this.encoder = encoder;
    }

    //인증을 위한 메서드
    public boolean matches(String memberId, String password) {

        Member member = repository.findByMemberId(memberId);
        if (member == null) {
            return false;
        }
        return encoder.matches(password, member.getPassword());
    }

    //회원가입
    public void register(Member member) {
        if (repository.countByMemberId(member.getMemberId()) > 0) {
            throw new MemberAlreadyExistException(member.getMemberId() + " already exits");
        }
        String encodePassword = encoder.encode(member.getPassword());
        member.setPassword(encodePassword);
        repository.save(member);
    }

    // 아이디로 멤버 조회
    public Member getByMemberId(String memberId) {
        return repository.findByMemberId(memberId);
    }


    // 회원 상태 수정
    public Member updateStatus(String memberId, Status status) {
        Member member = repository.findByMemberId(memberId);
        member.setStatus(status);
        repository.save(member);
        return member;
    }


}

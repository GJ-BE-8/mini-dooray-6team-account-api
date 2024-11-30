package com.nhnacademy.account.repository;

import com.nhnacademy.account.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

    // 데이터베이스에 해당 id가 있는지
    int countByMemberId(String memberId);

    //해당 id인 멤버 가져오기
    Member findByMemberId(String memberId);

}

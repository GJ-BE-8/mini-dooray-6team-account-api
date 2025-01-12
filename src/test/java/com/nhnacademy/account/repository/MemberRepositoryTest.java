package com.nhnacademy.account.repository;

import com.nhnacademy.account.entity.member.Member;
import com.nhnacademy.account.entity.member.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private  MemberRepository repository;

    @Sql("data.sql")
    @Test
    void findByMemberIdTest()
    {
        String memberId = "12";

        Member member = repository.findByMemberId(memberId);

        assertThat(member).isNotNull();
        assertThat(member.getMemberId()).isEqualTo(memberId);
        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Sql("data.sql")
    @Test
    void countByMemberId()
    {
        String memberId = "123";

        int result = repository.countByMemberId(memberId);

        assertThat(result).isEqualTo(1);

    }
}

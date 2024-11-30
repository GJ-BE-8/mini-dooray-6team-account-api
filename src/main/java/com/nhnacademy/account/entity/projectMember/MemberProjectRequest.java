package com.nhnacademy.account.entity.projectMember;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MemberProjectRequest {

    private String memberId;
    private long projectId;

}

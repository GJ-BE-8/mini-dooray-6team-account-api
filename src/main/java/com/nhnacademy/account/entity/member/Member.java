package com.nhnacademy.account.entity.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @JsonProperty("memberId") // JSON 응답에서 "memberId"로 직렬화
    private String memberId;

    @NotNull
    @Setter
    @JsonProperty("name") // JSON 응답에서 "name"으로 직렬화
    private String name;

    @NotNull
    @Setter
    @JsonProperty("password") // JSON 응답에서 "password"로 직렬화
    private String password;

    @NotNull
    @Setter
    @JsonProperty("email") // JSON 응답에서 "email"로 직렬화
    private String email;

    @NotNull
    @Setter
    @Enumerated(EnumType.STRING)
    @JsonProperty("status") // JSON 응답에서 "status"로 직렬화
    private Status status;

    public Member(String memberId, String password, String email, String name) {
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.status = Status.ACTIVE;
        this.name = name;
    }
}

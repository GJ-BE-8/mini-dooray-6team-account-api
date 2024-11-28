package com.nhnacademy.account.entity;

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
    private String memberId;

    @NotNull
    @Setter
    private String name;

    @NotNull
    @Setter
    private String password;

    @NotNull
    @Setter
    private String email;


    @NotNull
    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;

    public Member(String memberId, String password, String email, String name) {
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.status = Status.ACTIVE;
        this.name = name;
    }
}

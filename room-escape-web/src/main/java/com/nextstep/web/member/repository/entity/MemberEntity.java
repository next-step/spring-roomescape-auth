package com.nextstep.web.member.repository.entity;

import lombok.Getter;
import nextstep.domain.Identity;
import nextstep.domain.member.Member;

@Getter
public class MemberEntity {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;

    public MemberEntity(String username, String password, String name, String phone) {
        new MemberEntity(null, username, password, name, phone);
    }

    public MemberEntity(Long id, String username, String password, String name, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public Member fromThis() {
        return new Member(new Identity(id),
                username,
                password,
                name,
                phone);
    }
}

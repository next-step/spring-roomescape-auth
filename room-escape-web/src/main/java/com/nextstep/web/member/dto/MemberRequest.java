package com.nextstep.web.member.dto;

import com.nextstep.web.member.repository.entity.MemberEntity;
import nextstep.domain.member.Member;

public class MemberRequest {
    private String username;
    private String password;
    private String name;
    private String phone;

    public MemberRequest(String username, String password, String name, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public MemberEntity toEntity() {
        return new MemberEntity(username, password, name, phone);
    }
}

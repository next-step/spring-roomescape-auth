package com.nextstep.web.common;

public class LoginMember {
    private Long id;

    private LoginMember(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static LoginMember from(String id) {
        return new LoginMember(Long.parseLong(id));
    }
}

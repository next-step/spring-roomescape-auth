package nextstep.core.member.in;

import nextstep.core.member.Member;

public class MemberRegisterRequest {
    private String username;
    private String password;
    private String name;
    private String phone;

    private MemberRegisterRequest() {
    }

    public MemberRegisterRequest(String username, String password, String name, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public Member to() {
        return new Member(username, password, name, phone);
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

}

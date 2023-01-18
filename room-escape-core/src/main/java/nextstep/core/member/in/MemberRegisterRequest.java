package nextstep.core.member.in;

import nextstep.core.member.Member;
import nextstep.core.member.MemberRole;

public class MemberRegisterRequest {
    private String username;
    private String password;
    private String name;
    private MemberRole role;
    private String phone;

    private MemberRegisterRequest() {
    }

    public MemberRegisterRequest(String username, String password, String name, MemberRole role, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.phone = phone;
    }

    public Member to() {
        return new Member(username, password, name, role, phone);
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

    public MemberRole getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

}

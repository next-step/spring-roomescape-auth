package nextstep.app.web.member;

import nextstep.core.member.MemberRole;
import nextstep.core.member.in.MemberRegisterRequest;

public class MemberRegisterWebRequest {
    private String username;
    private String password;
    private String name;
    private MemberRole role;
    private String phone;

    private MemberRegisterWebRequest() {
    }

    public MemberRegisterWebRequest(String username, String password, String name, MemberRole role, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.phone = phone;
    }

    public MemberRegisterRequest to() {
        return new MemberRegisterRequest(username, password, name, role, phone);
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

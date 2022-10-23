package nextstep.app.web.member;

import nextstep.core.member.in.MemberRegisterRequest;

public class MemberRegisterWebRequest {
    private String username;
    private String password;
    private String name;
    private String phone;

    private MemberRegisterWebRequest() {
    }

    public MemberRegisterWebRequest(String username, String password, String name, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public MemberRegisterRequest to() {
        return new MemberRegisterRequest(username, password, name, phone);
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

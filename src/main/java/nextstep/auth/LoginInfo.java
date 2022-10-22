package nextstep.auth;

import nextstep.member.Member;

public class LoginInfo {

    private String username;
    private String role;

    public LoginInfo() {
    }

    public LoginInfo(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public static LoginInfo of(Member member) {
        return new LoginInfo(member.getUsername(), member.getRole());
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}

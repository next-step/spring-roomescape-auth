package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.Role;

public class LoginInfo {

    private String username;
    private Role role;

    public LoginInfo() {
    }

    public LoginInfo(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public static LoginInfo of(Member member) {
        return new LoginInfo(member.getUsername(), member.getRole());
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }
}

package nextstep.application.controller.auth;

import java.util.List;

public class LoginMember {

    private String principal;
    private List<String> roles;

    private LoginMember() {
    }

    public LoginMember(String principal, List<String> roles) {
        this.principal = principal;
        this.roles = roles;
    }

    public String getPrincipal() {
        return principal;
    }

    public List<String> getRoles() {
        return roles;
    }
}

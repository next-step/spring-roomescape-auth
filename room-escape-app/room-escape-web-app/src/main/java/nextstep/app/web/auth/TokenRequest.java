package nextstep.app.web.auth;

import nextstep.core.member.in.MemberLoginRequest;

public class TokenRequest {
    private String username;
    private String password;

    private TokenRequest() {
    }

    public TokenRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public MemberLoginRequest to() {
        return new MemberLoginRequest(username, password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

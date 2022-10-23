package nextstep.auth;

import java.util.List;

public class TokenParseResponse {
    private String subject;
    private List<String> roles;

    public TokenParseResponse(String subject, List<String> roles) {
        this.subject = subject;
        this.roles = roles;
    }

    public String getSubject() {
        return subject;
    }

    public List<String> getRoles() {
        return roles;
    }
}
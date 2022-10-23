package nextstep.auth;

import java.util.List;

public class TokenRequest {
    private String subject;
    private List<String> roles;

    public TokenRequest(Long subject, List<String> roles) {
        this(String.valueOf(subject), roles);
    }

    public TokenRequest(String subject, List<String> roles) {
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
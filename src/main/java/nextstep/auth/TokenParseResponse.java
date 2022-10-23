package nextstep.auth;

import java.util.List;
import nextstep.member.MemberRole;

public class TokenParseResponse {
    private String subject;
    private List<MemberRole> roles;

    public TokenParseResponse(String subject, List<MemberRole> roles) {
        this.subject = subject;
        this.roles = roles;
    }

    public String getSubject() {
        return subject;
    }

    public List<MemberRole> getRoles() {
        return roles;
    }
}
package nextstep.auth;

import java.util.List;
import nextstep.member.MemberRole;

public class TokenRequest {
    private String subject;
    private List<MemberRole> roles;

    public TokenRequest(Long subject, List<MemberRole> roles) {
        this(String.valueOf(subject), roles);
    }

    public TokenRequest(String subject, List<MemberRole> roles) {
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
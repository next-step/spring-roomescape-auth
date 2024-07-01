package roomescape.login;

import roomescape.member.MemberRole;

public class LoginMember {

    private Long id;

    private String name;

    private MemberRole role;

    public LoginMember(Long id, String name, MemberRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MemberRole getRole() {
        return role;
    }
}

package roomescape.member.domain.entity;

import roomescape.member.domain.MemberRole;
import roomescape.member.ui.dto.MemberRequest;

public class Member {
    private Long id;
    private final String name;
    private final MemberRole role;
    private final String email;
    private final String password;

    public Member(Long id, String name, String roleName, String email, String password) {
        this.id = id;
        this.name = name;
        this.role = MemberRole.createByName(roleName);
        this.email = email;
        this.password = password;
    }

    public static Member of(Long id, String name, String roleName, String email, String password) {
        return new Member(id, name, roleName, email, password);
    }

    public static Member of(MemberRequest request, String roleName, String encodedPassword) {
        return new Member(null, request.name(), roleName, request.email(), encodedPassword);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public MemberRole getRole() {
        return role;
    }

    public String getRoleName() {
        return role.name();
    }

    public void setId(Long id) {
        this.id = id;
    }
}

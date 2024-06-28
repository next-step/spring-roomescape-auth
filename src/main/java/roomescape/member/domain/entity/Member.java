package roomescape.member.domain.entity;

import roomescape.member.ui.dto.MemberRequest;

public class Member {
    private Long id;
    private final String name;
    private final String email;
    private final String password;

    private Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static Member of(Long id, String name, String email, String password) {
        return new Member(id, name, email, password);
    }

    public static Member of(MemberRequest request, String encodedPassword) {
        return new Member(null, request.name(), request.email(), encodedPassword);
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

    public void setId(Long id) {
        this.id = id;
    }
}

package roomescape.domain;

import java.util.Objects;

public class Member {

    private Long id;
    private String name;
    private String email;
    private String password;
    private RoleType roleType;

    public Member(Long id, String name, String email, String password, RoleType roleType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleType = roleType;
    }

    public Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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

    public RoleType getRoleType() {
        return roleType;
    }

    public Member toEntity(Member member, Long id) {
        return new Member(id
                , member.getName()
                , member.getName()
                , member.getPassword()
                , member.getRoleType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

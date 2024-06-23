package roomescape.domain;

public class LoginMember {
    private String email;
    private String name;
    private RoleType roleType;

    public LoginMember(String email, String name, RoleType roleType) {
        this.email = email;
        this.name = name;
        this.roleType = roleType;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public RoleType getRoleType() {
        return roleType;
    }
}

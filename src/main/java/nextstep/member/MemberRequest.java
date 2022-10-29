package nextstep.member;

public class MemberRequest {
    private String username;
    private String password;
    private String name;
    private String phone;
    private Role role;

    public MemberRequest(String username, String password, String name, String phone, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public Member toEntity() {
        return new Member(username, password, name, phone, role);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }
}

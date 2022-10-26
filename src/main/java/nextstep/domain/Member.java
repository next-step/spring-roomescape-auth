package nextstep.domain;

import nextstep.common.Role;

public class Member {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Role role;

    public Member(String username, String password, String name, String phone) {
        this(null, username, password, name, phone, Role.USER);
    }

    public Member(String username, String password, String name, String phone, Role role) {
        this(null, username, password, name, phone, role);
    }

    public Member(
        Long id,
        String username,
        String password,
        String name,
        String phone,
        Role role
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    private Member() {
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }

    public Long getId() {
        return id;
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

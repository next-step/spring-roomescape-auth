package nextstep.member;

import java.util.List;

public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private List<String> roles;

    protected Member() {
    }

    public Member(Long id, String username, String password, String name, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public Member(String username, String password, String name, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public Member(Long id, String username, String password, String name, String phone, List<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.roles = roles;
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

    public List<String> getRoles() {
        return roles;
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}

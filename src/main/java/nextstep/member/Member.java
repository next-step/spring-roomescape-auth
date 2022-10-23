package nextstep.member;

import java.util.List;

public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String uuid;
    private List<String> roles;

    public Member(Long id, String username, String password, String name, String phone, String uuid, List<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.uuid = uuid;
        this.roles = roles;
    }

    public Member(String username, String password, String name, String phone, String uuid, List<String> roles) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public List<String> getRoles() {
        return roles;
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}

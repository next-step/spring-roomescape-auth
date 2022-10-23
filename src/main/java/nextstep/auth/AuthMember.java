package nextstep.auth;

public class AuthMember {

    private Long id;
    private String username;
    private String name;
    private String phone;
    private String role;

    public AuthMember(Long id, String username, String name, String phone, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }
}

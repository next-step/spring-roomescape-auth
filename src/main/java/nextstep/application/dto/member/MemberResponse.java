package nextstep.application.dto.member;

public class MemberResponse {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    private MemberResponse() {
    }

    public MemberResponse(
        Long id,
        String username,
        String password,
        String name,
        String phone,
        String role
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
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

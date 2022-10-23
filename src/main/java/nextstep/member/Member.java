package nextstep.member;

public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private MemberRole role;

    protected Member() {
    }

    public Member(String username, String password, String name, String phone, MemberRole role) {
        this(null, username, password, name, phone, role);
    }

    public Member(
        Long id,
        String username,
        String password,
        String name,
        String phone,
        MemberRole role
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public void checkWrongPassword(String password) {
        if (!this.password.equals(password)) {
            throw new IllegalArgumentException("사용자의 비밀번호가 올바르지 않습니다.");
        }
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

    public MemberRole getRole() {
        return role;
    }
}

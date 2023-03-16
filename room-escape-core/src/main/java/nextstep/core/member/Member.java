package nextstep.core.member;

public class Member {
    private final Long id;
    private final String username;
    private final String password;
    private final String name;
    private final MemberRole role;
    private final String phone;

    public Member(String username, String password, String name, MemberRole role, String phone) {
        this(null, username, password, name, role, phone);
    }

    public Member(Long id, String username, String password, String name, MemberRole role, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.phone = phone;
    }

    public boolean checkWrongPassword(String password) {
        return this.password.equals(password);
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

    public MemberRole getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }
}

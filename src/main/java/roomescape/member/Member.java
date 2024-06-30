package roomescape.member;

import org.thymeleaf.util.StringUtils;

public class Member {

    private Long id;

    private String email;

    private String password;

    private String name;

    private MemberRole role;

    public Member(Long id, String email, String password, String name, MemberRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Member(String email, String password, String name, MemberRole role) {
        this(null, email, password, name, role);
    }

    public boolean isMatchedPassword(String password) {
        return StringUtils.equals(this.password, password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
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
}

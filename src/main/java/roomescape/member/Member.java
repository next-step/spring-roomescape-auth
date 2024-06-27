package roomescape.member;

import org.thymeleaf.util.StringUtils;

public class Member {

    private Long id;

    private String email;

    private String password;

    private String name;

    public Member(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
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

    public String getName() {
        return name;
    }
}

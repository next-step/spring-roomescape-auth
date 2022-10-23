package nextstep.domain.member;

import nextstep.common.BusinessException;
import nextstep.domain.Identity;

import java.util.List;

public class Member {
    private final Identity id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;
    private final List<Role> roles;

    public Member(Identity id, String username, String password, String name, String phone, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.roles = roles;
    }

    public Long getId() {
        return id.getNumber();
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

    public List<Role> getRoles() { return roles; }

    public void validatePassword(String password) {
        if (!this.password.equals(password)) {
            throw new BusinessException("비밀번호가 일치하지 않습니다.");
        }
    }
}

package nextstep.domain.member;

import nextstep.common.BusinessException;
import nextstep.domain.Identity;

public class Member {
    private final Identity id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;

    public Member(Identity id, String username, String password, String name, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
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

    public void validatePassword(String password) {
        if (!this.password.equals(password)) {
            throw new BusinessException("");
        }
    }
}

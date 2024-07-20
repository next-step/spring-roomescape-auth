package roomescape.domain;

import roomescape.dto.reservation.create.ReservationCreateRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.custom.AuthorizationException;

import java.util.Optional;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;

    public void checkPassword(String password) {
        if (!this.password.equals(password)) {
            throw new AuthorizationException(ErrorCode.UNAUTHORIZED_USER, "패스워드가 일치하지 않습니다. 다시 입력해주세요.");
        }
    }

    public void checkUser() {
        if(Optional.ofNullable(this).isEmpty()) {
            throw new AuthorizationException(ErrorCode.UNAUTHORIZED_USER, "다시 로그인해주세요.");
        }
    }

    public User(Long id) {
        this.id = id;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}

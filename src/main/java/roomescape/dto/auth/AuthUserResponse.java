package roomescape.dto.auth;

import roomescape.domain.Role;
import roomescape.domain.User;

public class AuthUserResponse {
    private String name;
    private String email;
    private String password;
    private Role role;

    public AuthUserResponse(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
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

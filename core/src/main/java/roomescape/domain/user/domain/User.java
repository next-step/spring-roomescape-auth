package roomescape.domain.user.domain;

import lombok.Builder;
import lombok.Getter;
import roomescape.domain.common.exception.Assert;

import java.util.Objects;

@Getter
public class User {

    private final Long id;
    private final UserRole role;
    private final String name;
    private final String email;
    private final String password;

    @Builder
    private User(final Long id, UserRole role, final String name, final String email, final String password) {
        Assert.notNullField(role, "role");
        Assert.notNullField(name, "name");
        Assert.notNullField(email, "email");
        Assert.notNullField(password, "password");

        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean matchesPassword(final String password) {
        return Objects.nonNull(password) && this.password.equals(password);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

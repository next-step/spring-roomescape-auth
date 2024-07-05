package roomescape.repository.domain.user.entity;

import roomescape.domain.user.User;
import roomescape.domain.user.vo.UserEmail;
import roomescape.domain.user.vo.UserId;
import roomescape.domain.user.vo.UserName;
import roomescape.domain.user.vo.UserPassword;

public class UserEntity {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public UserEntity(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static UserEntity from(User user) {
        return new UserEntity(user.getId().id(), user.getName().name(), user.getEmail().email(), user.getPassword().password());
    }

    public User toDomain() {
        return new User(
                new UserId(this.id),
                new UserName(this.name),
                new UserEmail(this.email),
                new UserPassword(this.password)
        );
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

    public UserEntity withId(Long id) {
        return new UserEntity(id, this.name, this.email, this.password);
    }
}

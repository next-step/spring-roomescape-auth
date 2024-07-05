package roomescape.application.domain.user.service.command;

import roomescape.domain.user.User;
import roomescape.domain.user.vo.UserEmail;
import roomescape.domain.user.vo.UserId;
import roomescape.domain.user.vo.UserName;
import roomescape.domain.user.vo.UserPassword;

public class CreateUserCommand {

    private final String email;

    private final String password;

    private final String name;

    public CreateUserCommand(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User toUser() {
        return new User(UserId.empty(), new UserName(this.name), new UserEmail(this.email), new UserPassword(this.password));
    }
}

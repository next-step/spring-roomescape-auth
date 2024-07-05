package roomescape.application.domain.auth.service.command;

import roomescape.domain.user.vo.UserEmail;
import roomescape.domain.user.vo.UserPassword;

public class LoginCommand {

    private final String email;

    private final String password;

    public LoginCommand(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserEmail fetchEmail() {
        return new UserEmail(this.email);
    }

    public UserPassword fetchPassword() {
        return new UserPassword(this.password);
    }
}

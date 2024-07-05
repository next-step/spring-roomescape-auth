package roomescape.application.domain.user.service;

import org.springframework.stereotype.Service;
import roomescape.application.domain.user.service.command.CreateUserCommand;
import roomescape.application.domain.user.service.component.creator.UserCreator;
import roomescape.domain.user.User;

@Service
public class UserService {

    private final UserCreator userCreator;

    public UserService(UserCreator userCreator) {
        this.userCreator = userCreator;
    }

    public User create(CreateUserCommand command) {
        return userCreator.create(command.toUser());
    }
}

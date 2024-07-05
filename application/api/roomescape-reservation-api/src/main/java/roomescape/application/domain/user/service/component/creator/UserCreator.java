package roomescape.application.domain.user.service.component.creator;

import org.springframework.stereotype.Component;
import roomescape.application.domain.user.service.component.validator.CreateUserValidator;
import roomescape.auth.password.encoder.UserPasswordEncoder;
import roomescape.domain.user.User;
import roomescape.domain.user.UserRepository;
import roomescape.domain.user.vo.UserPassword;

@Component
public class UserCreator {

    private final UserRepository userRepository;
    private final CreateUserValidator validator;
    private final UserPasswordEncoder passwordEncoder;


    public UserCreator(
            UserRepository userRepository,
            CreateUserValidator validator,
            UserPasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        validator.validate(user);
        UserPassword encodedPassword = passwordEncoder.encode(user.getPassword());

        return userRepository.save(user.withPassword(encodedPassword));
    }
}

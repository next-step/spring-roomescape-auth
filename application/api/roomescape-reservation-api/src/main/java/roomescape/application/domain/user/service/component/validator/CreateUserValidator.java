package roomescape.application.domain.user.service.component.validator;

import org.springframework.stereotype.Component;
import roomescape.domain.user.User;
import roomescape.domain.user.UserRepository;
import roomescape.error.exception.CreateUserValidateException;

@Component
public class CreateUserValidator {

    private final UserRepository repository;

    public CreateUserValidator(UserRepository repository) {
        this.repository = repository;
    }

    public void validate(User user) {
        this.validateExistEmail(user);
    }

    private void validateExistEmail(User user) {
        repository.findByEmail(user.getEmail())
                .ifPresent(findUser -> {
                    throw CreateUserValidateException.existEmail(user.getEmail());
                });
    }
}

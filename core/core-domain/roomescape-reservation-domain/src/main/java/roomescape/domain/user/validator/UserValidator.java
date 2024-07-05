package roomescape.domain.user.validator;

import roomescape.domain.user.vo.UserEmail;
import roomescape.domain.user.vo.UserId;
import roomescape.domain.user.vo.UserName;
import roomescape.domain.user.vo.UserPassword;
import roomescape.error.exception.DomainValidateException;

import java.util.Objects;

public final class UserValidator {

    private UserValidator() {
        throw new UnsupportedOperationException(this.getClass().getName() + "의 인스턴스는 생성되어서 안됩니다.");
    }

    public static void validate(UserId id, UserName name, UserEmail email, UserPassword password) {
        validateNotNull(id, name, email, password);
        validateNotEmpty(name, email, password);
    }

    private static void validateNotEmpty(UserName name, UserEmail email, UserPassword password) {
        if (name.isEmpty()) {
            throw DomainValidateException.notToBeEmpty(name.getClass());
        }
        if (email.isEmpty()) {
            throw DomainValidateException.notToBeEmpty(email.getClass());
        }
        if (password.isEmpty()) {
            throw DomainValidateException.notToBeEmpty(password.getClass());
        }
    }

    private static void validateNotNull(UserId id, UserName name, UserEmail email, UserPassword password) {
        if (Objects.isNull(id)) {
            throw DomainValidateException.notToBeNull(UserId.class);
        }
        if (Objects.isNull(name)) {
            throw DomainValidateException.notToBeNull(UserName.class);
        }
        if (Objects.isNull(email)) {
            throw DomainValidateException.notToBeNull(UserEmail.class);
        }
        if (Objects.isNull(password)) {
            throw DomainValidateException.notToBeNull(UserPassword.class);
        }
    }
}

package roomescape.error.exception;

import roomescape.domain.user.vo.UserEmail;
import roomescape.error.code.DomainErrorCode;
import roomescape.error.key.DomainErrorKey;
import roomescape.error.key.DomainErrorKeys;

public class CreateUserValidateException extends DomainException {

    private static final String ERROR_KEY_NAME_EMAIL = "email";

    public CreateUserValidateException(DomainErrorCode code, DomainErrorKeys keys) {
        super(code, keys);
    }

    public static CreateUserValidateException existEmail(UserEmail email) {
        return new CreateUserValidateException(
                DomainErrorCode.CANNOT_CREATE_USER_EXIST_EMAIL,
                DomainErrorKeys.of(
                        new DomainErrorKey(ERROR_KEY_NAME_EMAIL, email.email())
                )
        );
    }
}

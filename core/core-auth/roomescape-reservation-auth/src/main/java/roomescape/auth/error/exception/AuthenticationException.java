package roomescape.auth.error.exception;

import roomescape.auth.error.code.AuthErrorCode;
import roomescape.auth.error.key.AuthErrorKey;
import roomescape.auth.error.key.AuthErrorKeys;
import roomescape.domain.user.vo.UserPassword;

public class AuthenticationException extends AuthException {

    private static final String ERROR_KEY_PASSWORD = "password";

    public AuthenticationException(AuthErrorCode code, AuthErrorKeys keys) {
        super(code, keys);
    }

    public static AuthenticationException notMatchPassword(UserPassword password) {
        return new AuthenticationException(
                AuthErrorCode.NOT_MATCH_PASSWORD,
                AuthErrorKeys.of(
                        new AuthErrorKey(
                                ERROR_KEY_PASSWORD,
                                password.password()
                        )
                )
        );
    }
}

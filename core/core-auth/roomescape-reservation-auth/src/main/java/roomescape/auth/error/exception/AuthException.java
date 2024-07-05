package roomescape.auth.error.exception;

import roomescape.auth.error.code.AuthErrorCode;
import roomescape.auth.error.key.AuthErrorKeys;

public class AuthException extends RuntimeException {

    private final AuthErrorCode code;

    private final AuthErrorKeys keys;

    public AuthException(AuthErrorCode code, AuthErrorKeys keys) {
        super(keys.toMessage() + code.getMessage());
        this.code = code;
        this.keys = keys;
    }

    public AuthErrorCode getCode() {
        return code;
    }

    public String getCodeName() {
        return this.code.name();
    }
}

package roomescape.error.exception;

import roomescape.error.RoomescapeErrorMessage;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super(RoomescapeErrorMessage.AUTHENTICATION_EXCEPTION);
    }
}

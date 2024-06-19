package roomescape.error.exception;

import roomescape.error.RoomescapeErrorMessage;

public class PasswordNotMatchedException extends RuntimeException {

    public PasswordNotMatchedException() {
        super(RoomescapeErrorMessage.NOT_MATCHED_PASSWORD_EXCEPTION);
    }
}

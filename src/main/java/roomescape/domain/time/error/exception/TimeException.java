package roomescape.domain.time.error.exception;

import roomescape.global.error.exception.UserException;

public class TimeException extends UserException {

    public TimeException(TimeErrorCode timeErrorCode) {
        super(timeErrorCode.getStatus(), timeErrorCode.getErrorMessage());
    }

    @Override
    public int getStatus() {
        return super.getStatus();
    }
}

package roomescape.domain.theme.error.exception;

import roomescape.global.error.exception.UserException;

public class ThemeException extends UserException {

    public ThemeException(ThemeErrorCode themeErrorCode) {
        super(themeErrorCode.getStatus(), themeErrorCode.getErrorMessage());
    }

    @Override
    public int getStatus() {
        return super.getStatus();
    }
}


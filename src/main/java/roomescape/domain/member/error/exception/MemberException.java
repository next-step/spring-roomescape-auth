package roomescape.domain.member.error.exception;

import roomescape.global.error.exception.UserException;

public class MemberException extends UserException {

    public MemberException(MemberErrorCode themeErrorCode) {
        super(themeErrorCode.getStatus(), themeErrorCode.getErrorMessage());
    }

    @Override
    public int getStatus() {
        return super.getStatus();
    }
}

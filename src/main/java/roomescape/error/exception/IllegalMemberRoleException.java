package roomescape.error.exception;

import roomescape.error.RoomescapeErrorMessage;

public class IllegalMemberRoleException extends IllegalArgumentException {

    public IllegalMemberRoleException() {
        super(RoomescapeErrorMessage.ILLEGAL_MEMBER_ROLE_EXCEPTION);
    }
}

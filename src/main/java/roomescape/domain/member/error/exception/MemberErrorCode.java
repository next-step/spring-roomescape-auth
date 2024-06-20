package roomescape.domain.member.error.exception;

import org.springframework.http.HttpStatus;

public enum MemberErrorCode {
    NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST.value(), "아이디 혹은 비밀번호가 맞지 않습니다."),
    INVALID_MEMBER_DETAILS_ERROR(HttpStatus.BAD_REQUEST.value(), "사용자의 정보가 올바르지 않습니다."),
    NO_MEMBER_ERROR(HttpStatus.BAD_REQUEST.value(), "사용자가 존재하지 않습니다.");

    private final int status;
    private final String errorMessage;

    MemberErrorCode(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

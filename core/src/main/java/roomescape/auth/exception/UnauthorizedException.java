package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.domain.common.exception.BusinessException;
import roomescape.domain.common.exception.CustomErrorCode;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(final String message) {
        this(HttpStatus.UNAUTHORIZED.value(), CustomErrorCode.UNAUTHORIZED, message);
    }

    protected UnauthorizedException(final int statusCode, final CustomErrorCode errorCode, final String message) {
        super(statusCode, errorCode, message);
    }
}

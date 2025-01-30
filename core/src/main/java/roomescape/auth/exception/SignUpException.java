package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.domain.common.exception.BusinessException;
import roomescape.domain.common.exception.CustomErrorCode;

public class SignUpException extends BusinessException {

    public SignUpException(final String message) {
        super(HttpStatus.BAD_REQUEST.value(), CustomErrorCode.A400, message);
    }
}

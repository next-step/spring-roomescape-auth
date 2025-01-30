package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.domain.common.exception.CustomErrorCode;

public class InvalidLoginRequestException extends UnauthorizedException {

    public InvalidLoginRequestException(final String message) {
        super(HttpStatus.BAD_REQUEST.value(), CustomErrorCode.A401, message);
    }
}

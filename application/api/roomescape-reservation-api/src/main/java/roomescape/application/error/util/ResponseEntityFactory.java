package roomescape.application.error.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import roomescape.application.error.dto.ErrorResponse;
import roomescape.application.error.exception.ApplicationException;
import roomescape.auth.error.exception.AuthException;
import roomescape.error.exception.DomainException;
import roomescape.error.exception.NotFoundDomainException;

import static org.springframework.http.HttpStatus.*;
import static roomescape.application.error.code.ApplicationErrorCode.RUN_TIME_EXCEPTION;

public final class ResponseEntityFactory {

    private ResponseEntityFactory() {
        throw new UnsupportedOperationException(this.getClass().getName() + "의 인스턴스는 생성되어서 안됩니다.");
    }

    public static ResponseEntity<ErrorResponse> internalServerError(RuntimeException exception) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(RUN_TIME_EXCEPTION.name(), exception.getMessage()));
    }

    public static ResponseEntity<ErrorResponse> badRequest(BindException ex) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.from(ex));
    }

    public static ResponseEntity<ErrorResponse> badRequest(ApplicationException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.from(exception));
    }

    public static ResponseEntity<ErrorResponse> badRequest(DomainException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.from(exception));
    }

    public static ResponseEntity<ErrorResponse> notFound(NotFoundDomainException exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ErrorResponse.from(exception));
    }

    public static ResponseEntity<ErrorResponse> unauthorized(AuthException exception) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ErrorResponse.from(exception));
    }
}

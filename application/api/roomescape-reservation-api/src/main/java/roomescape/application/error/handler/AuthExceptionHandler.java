package roomescape.application.error.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.application.error.dto.ErrorResponse;
import roomescape.application.error.logger.ExceptionLogger;
import roomescape.application.error.util.ResponseEntityFactory;
import roomescape.auth.error.exception.AuthenticationException;

@ControllerAdvice
public class AuthExceptionHandler {

    private final ExceptionLogger exceptionLogger = new ExceptionLogger(this.getClass());


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleException(AuthenticationException ex) {
        exceptionLogger.log(ex);

        return ResponseEntityFactory.unauthorized(ex);
    }
}

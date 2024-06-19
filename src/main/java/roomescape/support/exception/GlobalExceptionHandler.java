package roomescape.support.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.apply.auth.application.exception.IllegalTokenException;
import roomescape.apply.auth.application.exception.TokenNotFoundException;
import roomescape.apply.reservation.application.excpetion.DuplicateReservationException;
import roomescape.apply.reservation.application.excpetion.NotFoundReservationException;
import roomescape.apply.reservationtime.application.exception.NotFoundReservationTimeException;
import roomescape.apply.reservationtime.application.exception.ReservationTimeReferencedException;
import roomescape.apply.theme.application.exception.NotFoundThemeException;
import roomescape.apply.theme.application.exception.ThemeReferencedException;

import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "roomescape.apply")
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(IllegalTokenException.class)
    public ExceptionResponse handleIllegalTokenExceptionException(IllegalTokenException e) {
        return new ExceptionResponse(ExceptionCode.WRONG_TOKEN_ERROR.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenNotFoundException.class)
    public ExceptionResponse handleTokenNotFoundException(TokenNotFoundException e) {
        int unAuthorizedCode = HttpStatus.UNAUTHORIZED.value();
        return new ExceptionResponse(unAuthorizedCode, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class, IllegalArgumentException.class,
                        ReservationTimeReferencedException.class, ThemeReferencedException.class
    })
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("IllegalArgumentException caught: {}", e.getMessage());
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateReservationException.class)
    public ExceptionResponse handleDuplicateReservationException(DuplicateReservationException e) {
        logger.error("DuplicateReservationException caught: {} ", e.getMessage());
        return new ExceptionResponse(ExceptionCode.DUPLICATED_ERROR.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("HttpMessageNotReadableException caught: {}", e.getMessage());
        String errorMsg = "JSON parse error: " + e.getMostSpecificCause().getMessage();
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), errorMsg);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundThemeException.class, NotFoundReservationTimeException.class,
                       NotFoundReservationException.class, NoSuchElementException.class})
    public ExceptionResponse handleNoSuchElementException(NoSuchElementException e) {
        logger.error("NoSuchElementException caught: {}", e.getMessage());
        return new ExceptionResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ExceptionResponse handleRuntimeException(RuntimeException e) {
        logger.error("RuntimeException caught: ", e);
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server Error");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ExceptionResponse handleException(Exception ex) {
        logger.error("Exception caught: ", ex);
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server Error");
    }

    public record ExceptionResponse(
            int status,
            String message

    ) {

    }
}

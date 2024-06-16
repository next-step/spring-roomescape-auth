//package roomescape.adapter.in.web.advice;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import roomescape.exception.AuthenticationException;
//import roomescape.exception.AuthorizationException;
//import roomescape.exception.InvalidSaveDuplicationReservationTime;
//import roomescape.exception.NotFoundReservationException;
//import roomescape.exception.NotFoundReservationTimeException;
//import roomescape.exception.NotFoundThemeException;
//import roomescape.exception.ReservationTimeConflictException;
//
//@RestControllerAdvice
//public class ExceptionController {
//
//  @ExceptionHandler({
//    NotFoundReservationException.class, NotFoundReservationTimeException.class,
//    NotFoundThemeException.class, InvalidSaveDuplicationReservationTime.class,
//    ReservationTimeConflictException.class, IllegalArgumentException.class
//  })
//  public ResponseEntity<Void> handleException(RuntimeException e) {
//    return ResponseEntity.badRequest()
//                         .build();
//  }
//
//  @ExceptionHandler(AuthenticationException.class)
//  public ResponseEntity<Void> handleException(AuthenticationException e) {
//    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                         .build();
//  }
//
//  @ExceptionHandler(AuthorizationException.class)
//  public ResponseEntity<Void> handleException(AuthorizationException e) {
//    return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                         .build();
//  }
//
//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<Void> handleException(Exception e) {
//    return ResponseEntity.internalServerError()
//                         .build();
//  }
//}

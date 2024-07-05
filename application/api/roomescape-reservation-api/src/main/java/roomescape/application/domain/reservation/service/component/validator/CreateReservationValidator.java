package roomescape.application.domain.reservation.service.component.validator;

import org.springframework.stereotype.Component;
import roomescape.application.error.exception.CreateReservationValidateException;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;

@Component
public class CreateReservationValidator {

    private final ReservationRepository repository;

    public CreateReservationValidator(ReservationRepository repository) {
        this.repository = repository;
    }

    public void validate(ReservationDate date, ReservationTime reservationTime) {
        this.validateExistTime(date, reservationTime);
    }

    private void validateExistTime(ReservationDate date, ReservationTime time) {
        if (repository.existByDateAndTimeId(date, time.getId())) {
            throw CreateReservationValidateException.existDateTime(date, time);
        }
    }
}

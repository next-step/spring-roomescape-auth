package roomescape.application.domain.reservationtime.service.component.validator;

import org.springframework.stereotype.Component;
import roomescape.application.error.exception.CreateReservationTimeValidateException;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;

@Component
public class CreateReservationTimeValidator {

    private final ReservationTimeRepository timeRepository;


    public CreateReservationTimeValidator(
            ReservationTimeRepository timeRepository
    ) {
        this.timeRepository = timeRepository;
    }

    public void validate(ReservationTime reservationTime) {
        if (timeRepository.existByStartAt(reservationTime.getStartAt())) {
            throw CreateReservationTimeValidateException.existSameStartAt(reservationTime.getStartAt());
        }
    }
}

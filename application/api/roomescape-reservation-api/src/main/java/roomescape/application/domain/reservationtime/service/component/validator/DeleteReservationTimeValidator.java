package roomescape.application.domain.reservationtime.service.component.validator;

import org.springframework.stereotype.Component;
import roomescape.application.error.exception.DeleteReservationTimeValidateException;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservationtime.vo.ReservationTimeId;

@Component
public class DeleteReservationTimeValidator {

    private final ReservationRepository reservationRepository;

    public DeleteReservationTimeValidator(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void validate(ReservationTimeId timeId) {
        if (reservationRepository.existByTimeId(timeId)) {
            throw DeleteReservationTimeValidateException.existReservation(timeId);
        }
    }
}

package nextstep.application.service.reservation;

import java.util.Optional;
import nextstep.application.dto.reservation.ReservationRequest;
import nextstep.common.exception.ReservationException;
import nextstep.domain.Reservation;
import nextstep.domain.service.ReservationDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationCommandService {

    private final ReservationDomainService reservationDomainService;

    public ReservationCommandService(ReservationDomainService reservationDomainService) {
        this.reservationDomainService = reservationDomainService;
    }

    public Long make(ReservationRequest request) {
        String date = request.getDate();
        String time = request.getTime();

        Optional<Reservation> reservation = reservationDomainService.findOptionalBy(date, time);

        if (reservation.isPresent()) {
            throw new ReservationException(String.format("%s에는 이미 예약이 차있습니다.", reservation));
        }
        reservationDomainService.save(toReservation(request));

        return reservationDomainService.findBy(date, time).getId();
    }

    private Reservation toReservation(ReservationRequest request) {
        return new Reservation(request.getDate(), request.getTime(), request.getName());
    }

    public void cancel(String date, String time) {
        Optional<Reservation> reservation = reservationDomainService.findOptionalBy(date, time);

        if (reservation.isPresent()) {
            reservationDomainService.delete(date, time);
            return;
        }
        throw new ReservationException("존재하는 예약이 없어 예약을 취소할 수 없습니다.");
    }

    public void cancelAll() {
        reservationDomainService.deleteAll();
    }
}

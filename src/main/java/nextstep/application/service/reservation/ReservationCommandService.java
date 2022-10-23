package nextstep.application.service.reservation;

import nextstep.application.dto.reservation.ReservationRequest;
import nextstep.domain.Reservation;
import nextstep.domain.service.ReservationDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationCommandService {

    private final ReservationDomainService reservationDomainService;

    public ReservationCommandService(
        ReservationDomainService reservationDomainService
    ) {
        this.reservationDomainService = reservationDomainService;
    }

    public Long make(ReservationRequest request, Long memberId) {
        Long scheduleId = request.getScheduleId();

        reservationDomainService.save(new Reservation(scheduleId, memberId));
        return reservationDomainService.findBy(scheduleId, memberId).getId();
    }

    public void cancel(Long id) {
        reservationDomainService.deleteBy(id);
    }

    public void cancelAll() {
        reservationDomainService.deleteAll();
    }
}

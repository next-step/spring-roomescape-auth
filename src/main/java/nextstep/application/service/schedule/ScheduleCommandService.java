package nextstep.application.service.schedule;

import nextstep.application.dto.reservation.ReservationRequest;
import nextstep.application.dto.schedule.ScheduleRequest;
import nextstep.application.service.reservation.ReservationCommandService;
import nextstep.application.service.reservation.ReservationQueryService;
import nextstep.common.exception.ScheduleException;
import nextstep.domain.Schedule;
import nextstep.domain.service.ScheduleDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ScheduleCommandService {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;
    private final ScheduleDomainService scheduleDomainService;

    public ScheduleCommandService(
        ReservationCommandService reservationCommandService,
        ReservationQueryService reservationQueryService,
        ScheduleDomainService scheduleDomainService
    ) {
        this.reservationCommandService = reservationCommandService;
        this.reservationQueryService = reservationQueryService;
        this.scheduleDomainService = scheduleDomainService;
    }

    public Long make(ScheduleRequest request) {
        Long reservationId = reservationCommandService.make(toReservationRequest(request));

        scheduleDomainService.save(toSchedule(request, reservationId));

        return scheduleDomainService.findByReservation(reservationId).getId();
    }

    private ReservationRequest toReservationRequest(ScheduleRequest request) {
        return new ReservationRequest(request.getDate(), request.getTime());
    }

    private Schedule toSchedule(ScheduleRequest request, Long reservationId) {
        return new Schedule(request.getThemeId(), reservationId);
    }

    public void cancel(Long id) {
        scheduleDomainService.findOptionalBySchedule(id)
            .ifPresentOrElse(schedule -> {
                if (reservationQueryService.exist(schedule.getReservationId())) {
                    throw new ScheduleException("스케줄을 삭제할 수 없습니다.");
                }
                scheduleDomainService.delete(id);
            }, () -> {
                throw new ScheduleException("존재하지 않는 스케줄입니다.");
            });
    }

    public void cancelAll() {
        scheduleDomainService.deleteAll();
    }
}

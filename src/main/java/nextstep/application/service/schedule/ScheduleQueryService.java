package nextstep.application.service.schedule;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.application.dto.reservation.ReservationResponse;
import nextstep.application.dto.schedule.ScheduleResponse;
import nextstep.application.dto.theme.ThemeResponse;
import nextstep.application.service.reservation.ReservationQueryService;
import nextstep.application.service.theme.ThemeQueryService;
import nextstep.domain.Schedule;
import nextstep.domain.service.ScheduleDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ScheduleQueryService {

    private final ReservationQueryService reservationQueryService;
    private final ThemeQueryService themeQueryService;
    private final ScheduleDomainService scheduleDomainService;

    public ScheduleQueryService(
        ReservationQueryService reservationQueryService,
        ThemeQueryService themeQueryService,
        ScheduleDomainService scheduleDomainService
    ) {
        this.reservationQueryService = reservationQueryService;
        this.themeQueryService = themeQueryService;
        this.scheduleDomainService = scheduleDomainService;
    }

    public List<ScheduleResponse> checkAll(Long themeId, String date) {
        List<ReservationResponse> reservationResponses = reservationQueryService.checkAll(date);
        ThemeResponse themeResponse = themeQueryService.checkBy(themeId);

        return reservationResponses.stream()
            .map(reservationResponse -> {
                Long reservationId = reservationResponse.getId();
                Schedule schedule = scheduleDomainService.findBy(themeId, reservationId);
                return toScheduleResponse(themeResponse, reservationResponse, schedule);
            })
            .collect(Collectors.toList());
    }

    private ScheduleResponse toScheduleResponse(
        ThemeResponse themeResponse,
        ReservationResponse reservationResponse,
        Schedule schedule
    ) {
        return new ScheduleResponse(
            schedule.getId(),
            themeResponse,
            reservationResponse.getDate(),
            reservationResponse.getTime()
        );
    }
}

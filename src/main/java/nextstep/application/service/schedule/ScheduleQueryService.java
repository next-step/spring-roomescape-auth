package nextstep.application.service.schedule;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.application.dto.schedule.ScheduleResponse;
import nextstep.application.dto.theme.ThemeResponse;
import nextstep.application.service.theme.ThemeQueryService;
import nextstep.domain.Schedule;
import nextstep.domain.service.ScheduleDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ScheduleQueryService {

    private final ThemeQueryService themeQueryService;
    private final ScheduleDomainService scheduleDomainService;

    public ScheduleQueryService(
        ThemeQueryService themeQueryService,
        ScheduleDomainService scheduleDomainService
    ) {
        this.themeQueryService = themeQueryService;
        this.scheduleDomainService = scheduleDomainService;
    }

    public List<ScheduleResponse> checkAll(Long themeId, String date) {
        return scheduleDomainService.findAllBy(themeId, date).stream()
            .map(schedule -> {
                ThemeResponse themeResponse = themeQueryService.checkBy(schedule.getThemeId());
                return toScheduleResponse(schedule, themeResponse);
            })
            .collect(Collectors.toList());
    }

    private ScheduleResponse toScheduleResponse(Schedule schedule, ThemeResponse themeResponse) {
        return new ScheduleResponse(
            schedule.getId(),
            themeResponse,
            schedule.getDate().toString(),
            schedule.getTime().toString()
        );
    }
}

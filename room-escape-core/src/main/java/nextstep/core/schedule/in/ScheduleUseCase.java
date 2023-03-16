package nextstep.core.schedule.in;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleUseCase {
    ScheduleResponse create(ScheduleCreateRequest request);

    ScheduleResponse findByThemeIdAndDate(Long themeId, LocalDate date);

    List<ScheduleResponse> list(Long themeId, LocalDate date);

    boolean exists(Long scheduleId);

    void delete(Long scheduleId);
}

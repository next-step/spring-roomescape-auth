package nextstep.core.schedule.out;

import nextstep.core.schedule.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);

    Schedule findByThemeIdAndDate(Long themeId, LocalDate date);

    List<Schedule> findAllByThemeIdAndDate(Long themeId, LocalDate date);

    Boolean existsById(Long scheduleId);

    void deleteById(Long scheduleId);
}

package nextstep.domain.schedule.usecase;

import nextstep.domain.schedule.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    Long save(Schedule schedule);
    Optional<Schedule> findById(Long id);
    List<Schedule> findAllBy(Long themeId, LocalDate date);
    void delete(Long id);
}

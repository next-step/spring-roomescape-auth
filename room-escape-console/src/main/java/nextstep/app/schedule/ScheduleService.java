package nextstep.app.schedule;

import nextstep.common.BusinessException;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.schedule.usecase.ScheduleRepository;
import nextstep.domain.theme.Theme;
import nextstep.domain.theme.usecase.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ThemeRepository themeRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, ThemeRepository themeRepository) {
        this.scheduleRepository = scheduleRepository;
        this.themeRepository = themeRepository;
    }

    public void save(Long themeId, LocalDate date, LocalTime time) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new BusinessException(""));
        scheduleRepository.save(new Schedule(null, theme, date, time));
    }

    public List<Schedule> findAllBy(Long themeId, LocalDate date) {
        return scheduleRepository.findAllBy(themeId, date);
    }

    public void delete(Long reservationId) {
        scheduleRepository.delete(reservationId);
    }
}

package nextstep.app.web.schedule.adapter.in;

import nextstep.core.schedule.Schedule;
import nextstep.core.schedule.in.ScheduleCreateRequest;
import nextstep.core.schedule.in.ScheduleResponse;
import nextstep.core.schedule.in.ScheduleUseCase;
import nextstep.core.schedule.out.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ScheduleService implements ScheduleUseCase {
    private final ScheduleRepository repository;

    public ScheduleService(ScheduleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ScheduleResponse create(ScheduleCreateRequest request) {
        Objects.requireNonNull(request);
        Schedule schedule = request.to();

        Schedule saved = repository.save(schedule);
        return ScheduleResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public ScheduleResponse findByThemeIdAndDate(Long themeId, LocalDate date) {
        Objects.requireNonNull(themeId);
        Objects.requireNonNull(date);

        Schedule schedule = repository.findByThemeIdAndDate(themeId, date);
        return ScheduleResponse.from(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> list(Long themeId, LocalDate date) {
        Objects.requireNonNull(themeId);
        Objects.requireNonNull(date);

        return repository.findAllByThemeIdAndDate(themeId, date)
                .stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean exists(Long scheduleId) {
        Objects.requireNonNull(scheduleId);
        return repository.existsById(scheduleId);
    }

    @Transactional
    public void delete(Long scheduleId) {
        Objects.requireNonNull(scheduleId);
        repository.deleteById(scheduleId);
    }
}

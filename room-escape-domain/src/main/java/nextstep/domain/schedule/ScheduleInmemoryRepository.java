package nextstep.domain.schedule;

import nextstep.domain.schedule.usecase.ScheduleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ScheduleInmemoryRepository implements ScheduleRepository {
    @Override
    public Long save(Schedule schedule) {
        return null;
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Schedule> findAllBy(Long themeId, LocalDate date) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}

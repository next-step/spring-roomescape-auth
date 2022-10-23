package nextstep.domain.repository;

import java.util.List;
import java.util.Optional;
import nextstep.domain.Schedule;

public interface ScheduleRepository {

    void save(Schedule schedule);

    Optional<Schedule> findBy(Long id, String date, String time);

    List<Schedule> findAllBy(Long themeId, String date);

    void delete(Long id);

    void deleteAll();
}

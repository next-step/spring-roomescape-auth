package nextstep.domain.repository;

import java.util.Optional;
import nextstep.domain.Schedule;

public interface ScheduleRepository {

    void save(Schedule schedule);

    Optional<Schedule> findByReservation(Long id);

    Optional<Schedule> findBySchedule(Long id);

    Optional<Schedule> findBy(Long themeId, Long reservationId);

    void delete(Long id);

    void deleteAll();
}

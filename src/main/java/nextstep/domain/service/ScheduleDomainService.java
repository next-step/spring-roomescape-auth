package nextstep.domain.service;

import java.util.Optional;
import nextstep.common.exception.ScheduleException;
import nextstep.domain.Schedule;
import nextstep.domain.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

@Service
public class ScheduleDomainService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleDomainService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Optional<Schedule> findOptionalBySchedule(Long id) {
        return scheduleRepository.findBySchedule(id);
    }

    public Schedule findByReservation(Long id) {
        return scheduleRepository.findByReservation(id)
            .orElseThrow(() -> new ScheduleException("존재하지 않는 스케줄입니다."));
    }

    public Schedule findBy(Long themeId, Long reservationId) {
        return scheduleRepository.findBy(themeId, reservationId)
            .orElseThrow(() -> new ScheduleException("존재하지 않는 스케줄입니다."));
    }

    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public void delete(Long id) {
        scheduleRepository.delete(id);
    }

    public void deleteAll() {
        scheduleRepository.deleteAll();
    }
}

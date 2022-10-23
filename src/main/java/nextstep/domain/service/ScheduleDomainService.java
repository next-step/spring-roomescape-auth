package nextstep.domain.service;

import java.util.List;
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

    public Schedule findBy(Long themeId, String date, String time) {
        return scheduleRepository.findBy(themeId, date, time)
            .orElseThrow(() -> new ScheduleException("존재하지 않는 스케줄입니다."));
    }

    public List<Schedule> findAllBy(Long themeId, String date) {
        return scheduleRepository.findAllBy(themeId, date);
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

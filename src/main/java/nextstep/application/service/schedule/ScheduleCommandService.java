package nextstep.application.service.schedule;

import nextstep.application.dto.schedule.ScheduleRequest;
import nextstep.domain.Schedule;
import nextstep.domain.service.ScheduleDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ScheduleCommandService {

    private final ScheduleDomainService scheduleDomainService;

    public ScheduleCommandService(ScheduleDomainService scheduleDomainService) {
        this.scheduleDomainService = scheduleDomainService;
    }

    public Long make(ScheduleRequest request) {
        Long themeId = request.getThemeId();
        String date = request.getDate();
        String time = request.getTime();

        scheduleDomainService.save(new Schedule(themeId, date, time));
        return scheduleDomainService.findBy(themeId, date, time).getId();
    }

    public void delete(Long id) {
        scheduleDomainService.delete(id);
    }

    public void cancelAll() {
        scheduleDomainService.deleteAll();
    }
}

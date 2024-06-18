package roomescape.domain.time.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.service.ThemeService;
import roomescape.domain.time.service.dto.TimeRequest;
import roomescape.domain.time.domain.Time;
import roomescape.domain.time.domain.repository.TimeRepository;

import java.util.List;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ThemeService themeService;

    public TimeService(TimeRepository timeRepository, ThemeService themeService) {
        this.timeRepository = timeRepository;
        this.themeService = themeService;
    }

    @Transactional
    public Time save(TimeRequest timeRequest) {
        Theme theme = themeService.findById(timeRequest.getThemeId());
        Time time = new Time(null, timeRequest.getDate(), theme, timeRequest.getStartAt());
        Long id = timeRepository.save(time);
        return findById(id);
    }

    @Transactional
    public Time findById(Long id) {
        return timeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        timeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Time> findByDateAndThemeId(String date, String themeId) {
        return timeRepository.findByDateAndThemeId(date, themeId);
    }
}

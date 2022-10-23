package com.nextstep.web.schedule.service;

import com.nextstep.web.schedule.dto.CreateScheduleRequest;
import com.nextstep.web.theme.exception.ThemeNotFoundException;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.schedule.usecase.ScheduleRepository;
import nextstep.domain.theme.Theme;
import nextstep.domain.theme.usecase.ThemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduleCommandService {
    private final ThemeRepository themeRepository;
    private final ScheduleRepository scheduleRepository;

    public ScheduleCommandService(ThemeRepository themeRepository, ScheduleRepository scheduleRepository) {
        this.themeRepository = themeRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public Long save(CreateScheduleRequest request) {
        Theme theme = themeRepository.findById(request.getThemeId())
                .orElseThrow(ThemeNotFoundException::new);
        return scheduleRepository.save(new Schedule(null, theme, request.getDate(),
                request.getTime()));
    }

    public void delete(Long id) {
        scheduleRepository.delete(id);
    }
}

package com.nextstep.web.schedule.repository;

import com.nextstep.web.schedule.repository.entity.ScheduleEntity;
import com.nextstep.web.theme.repository.ThemeDao;
import com.nextstep.web.theme.repository.entity.ThemeEntity;
import nextstep.common.BusinessException;
import nextstep.domain.Identity;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.schedule.usecase.ScheduleRepository;
import nextstep.domain.theme.Theme;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcScheduleRepository implements ScheduleRepository {
    private final ScheduleDao scheduleDao;

    public JdbcScheduleRepository(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    @Override
    public Long save(Schedule schedule) {
        Theme theme = schedule.getTheme();
        return scheduleDao.save(new ScheduleEntity(null, new ThemeEntity(theme.getId().getNumber(),
                theme.getName(), theme.getDesc(), theme.getPrice()),
                schedule.getDate().toString(),
                schedule.getTime().toString()));
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return scheduleDao.findById(id)
                .map(scheduleEntity -> new Schedule(new Identity(scheduleEntity.getId()),
                        scheduleEntity.getThemeEntity().fromThis(),
                        LocalDate.parse(scheduleEntity.getDate()),
                        LocalTime.parse(scheduleEntity.getTime())));
    }

    @Override
    public List<Schedule> findAllBy(Long themeId, LocalDate date) {
        return scheduleDao.findAllBy(themeId, date.toString()).stream()
                .map(scheduleEntity -> new Schedule(new Identity(scheduleEntity.getId()),
                        scheduleEntity.getThemeEntity().fromThis(),
                        LocalDate.parse(scheduleEntity.getDate()),
                        LocalTime.parse(scheduleEntity.getTime())))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        scheduleDao.delete(id);
    }
}

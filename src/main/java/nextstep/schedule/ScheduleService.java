package nextstep.schedule;

import java.util.List;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

  private final ScheduleDao scheduleDao;
  private final ThemeDao themeDao;

  public ScheduleService(ScheduleDao scheduleDao, ThemeDao themeDao) {
    this.scheduleDao = scheduleDao;
    this.themeDao = themeDao;
  }

  public Long create(ScheduleRequest scheduleRequest) {
    Theme theme = themeDao.findById(scheduleRequest.themeId());
    return scheduleDao.save(scheduleRequest.toEntity(theme));
  }

  public List<Schedule> findByThemeIdAndDate(Long themeId, String date) {
    return scheduleDao.findByThemeIdAndDate(themeId, date);
  }

  public void deleteById(Long id) {
    scheduleDao.deleteById(id);
  }
}

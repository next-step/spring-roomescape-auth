package nextstep;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
public class RoomEscapeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomEscapeApplication.class, args);
    }
}

//@Component
class Dummy {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private ThemeDao themeDao;

    @PostConstruct
    public void uu() {
        memberDao.save(new Member("username1", "password", "hi", "phone", "ADMIN"));
        memberDao.save(new Member("username2", "password", "hi", "phone", "ADMIN"));

        Long themeId = themeDao.save(new Theme("name", "desc", 1));
        Theme theme = themeDao.findById(themeId);

        Schedule schedule = new Schedule(theme, LocalDate.now(), LocalTime.now());
        scheduleDao.save(schedule);
    }
}

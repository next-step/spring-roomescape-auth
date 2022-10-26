package nextstep.infrastructure.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import nextstep.domain.Schedule;
import nextstep.domain.repository.ScheduleRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleDao implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Schedule> schedule = (rs, rowNum) -> new Schedule(
        rs.getLong("id"),
        rs.getLong("themeId"),
        rs.getString("date"),
        rs.getString("time")
    );

    @Override
    public void save(Schedule schedule) {
        jdbcTemplate.update(
            "insert into schedule (themeId, date, time) values (?, ?, ?)",
            schedule.getThemeId(),
            schedule.getDate(),
            schedule.getTime()
        );
    }

    @Override
    public Optional<Schedule> findBy(Long id, String date, String time) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                "select id, themeId, date, time from schedule where id = ? and date = ? and time = ?",
                schedule,
                id,
                LocalDate.parse(date),
                LocalTime.parse(time)
            ));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Schedule> findAllBy(Long themeId, String date) {
        return jdbcTemplate.query(
            "select id, themeId, date, time from schedule where themeId = ? and date = ?",
            schedule,
            themeId,
            LocalDate.parse(date)
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from schedule where id = ?", id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from schedule");
    }
}

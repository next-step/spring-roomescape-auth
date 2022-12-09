package nextstep.schedule;

import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduleDao {
    private JdbcTemplate jdbcTemplate;

    public ScheduleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Schedule> rowMapper = (resultSet, rowNum) -> new Schedule(
            resultSet.getLong("schedule.id"),
            new Theme(
                    resultSet.getLong("theme.id"),
                    resultSet.getString("theme.name"),
                    resultSet.getString("theme.desc"),
                    resultSet.getInt("theme.price")
            ),
            resultSet.getDate("schedule.date").toLocalDate(),
            resultSet.getTime("schedule.time").toLocalTime()
    );

    public Long save(Schedule schedule) {
        String sql = "INSERT INTO schedule (theme_id, date, time) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, schedule.getTheme().getId());
            ps.setDate(2, Date.valueOf(schedule.getDate()));
            ps.setTime(3, Time.valueOf(schedule.getTime()));
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Schedule findById(Long id) {
        String sql = "SELECT schedule.id, schedule.theme_id, schedule.date, schedule.time, theme.id, theme.name, theme.desc, theme.price " +
                "from schedule " +
                "inner join theme on schedule.theme_id = theme.id " +
                "where schedule.id = ?;";

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<Schedule> findByThemeIdAndDate(Long themeId, String date) {
        String sql = "SELECT schedule.id, schedule.theme_id, schedule.date, schedule.time, theme.id, theme.name, theme.desc, theme.price " +
                "from schedule " +
                "inner join theme on schedule.theme_id = theme.id " +
                "where schedule.theme_id = ? and schedule.date = ?;";

        return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(LocalDate.parse(date)));
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM schedule where id = ?;", id);
    }

}

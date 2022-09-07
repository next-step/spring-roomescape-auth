package nextstep.reservation;

import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.util.List;

@Component
public class ReservationDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, name) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setString(2, reservation.getName());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = "SELECT r.id, r.schedule_id, r.name, r.phone, r.number, s.id, s.theme_id, s.date, s.time, t.id, t.name, t.desc, t.price " +
                "from reservation as r " +
                "inner join schedule as s on r.schedule_id = s.id " +
                "inner join theme as t on s.theme_id = t.id " +
                "where t.id = ? and s.date = ?;";

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> new Reservation(
                        resultSet.getLong("r.id"),
                        new Schedule(
                                resultSet.getLong("s.id"),
                                new Theme(
                                        resultSet.getLong("t.id"),
                                        resultSet.getString("t.name"),
                                        resultSet.getString("t.desc"),
                                        resultSet.getInt("t.price")
                                ),
                                resultSet.getDate("s.date").toLocalDate(),
                                resultSet.getTime("s.time").toLocalTime()
                        ),
                        resultSet.getString("r.name")
                ), themeId, Date.valueOf(date));
    }

    public Reservation findById(Long id) {
        String sql = "SELECT r.id, r.schedule_id, r.name, r.phone, r.number, s.id, s.theme_id, s.date, s.time, t.id, t.name, t.desc, t.price " +
                "from reservation as r " +
                "inner join schedule as s on r.schedule_id = s.id " +
                "inner join theme as t on s.theme_id = t.id " +
                "where r.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> new Reservation(
                            resultSet.getLong("r.id"),
                            new Schedule(
                                    resultSet.getLong("s.id"),
                                    new Theme(
                                            resultSet.getLong("t.id"),
                                            resultSet.getString("t.name"),
                                            resultSet.getString("t.desc"),
                                            resultSet.getInt("t.price")
                                    ),
                                    resultSet.getDate("s.date").toLocalDate(),
                                    resultSet.getTime("s.time").toLocalTime()
                            ),
                            resultSet.getString("r.name")
                    ), id);
        } catch (Exception e) {
            return null;
        }
    }

    public Reservation findByScheduleId(Long id) {
        String sql = "SELECT r.id, r.schedule_id, r.name, r.phone, r.number, s.id, s.theme_id, s.date, s.time, t.id, t.name, t.desc, t.price " +
                "from reservation as r " +
                "inner join schedule as s on r.schedule_id = s.id " +
                "inner join theme as t on s.theme_id = t.id " +
                "where s.id = ?;";

        return jdbcTemplate.queryForObject(sql,
                (resultSet, rowNum) -> new Reservation(
                        resultSet.getLong("r.id"),
                        new Schedule(
                                resultSet.getLong("s.id"),
                                new Theme(
                                        resultSet.getLong("t.id"),
                                        resultSet.getString("t.name"),
                                        resultSet.getString("t.desc"),
                                        resultSet.getInt("t.price")
                                ),
                                resultSet.getDate("s.date").toLocalDate(),
                                resultSet.getTime("s.time").toLocalTime()
                        ),
                        resultSet.getString("r.name")
                ), id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }
}

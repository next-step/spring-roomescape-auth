package nextstep.reservation;

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
        String sql = "INSERT INTO reservation (theme_id, date, time, name) VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getTheme().getId());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setTime(3, Time.valueOf(reservation.getTime()));
            ps.setString(4, reservation.getName());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findByDate(String date) {
        String sql = "SELECT r.id, r.date, r.time, r.name, t.id, t.name, t.desc, t.price " +
                "from reservation as r " +
                "join theme as t on r.theme_id = t.id " +
                "where date = ?;";

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> new Reservation(
                        resultSet.getLong("r.id"),
                        new Theme(
                                resultSet.getLong("t.id"),
                                resultSet.getString("t.name"),
                                resultSet.getString("t.desc"),
                                resultSet.getInt("t.price")
                        ),
                        resultSet.getDate("r.date").toLocalDate(),
                        resultSet.getTime("r.time").toLocalTime(),
                        resultSet.getString("r.name")
                ), Date.valueOf(date));
    }

    public Reservation findByDateAndTime(String date, String time) {
        String sql = "SELECT r.id, r.date, r.time, r.name, t.id, t.name, t.desc, t.price " +
                "from reservation as r " +
                "join theme as t on r.theme_id = t.id " +
                "where date = ? and time = ?;";
        try {
            return jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> new Reservation(
                            resultSet.getLong("r.id"),
                            new Theme(
                                    resultSet.getLong("t.id"),
                                    resultSet.getString("t.name"),
                                    resultSet.getString("t.desc"),
                                    resultSet.getInt("t.price")
                            ),
                            resultSet.getDate("r.date").toLocalDate(),
                            resultSet.getTime("r.time").toLocalTime(),
                            resultSet.getString("r.name")
                    ), Date.valueOf(date), Time.valueOf(time + ":00"));
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteByDateAndTime(String date, String time) {
        String sql = "DELETE FROM reservation where date = ? and time = ?;";
        jdbcTemplate.update(sql, Date.valueOf(date), Time.valueOf(time + ":00"));
    }
}

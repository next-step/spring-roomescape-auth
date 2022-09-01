package nextstep.reservation;

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
        String sql = "INSERT INTO reservation (date, time, name) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setDate(1, Date.valueOf(reservation.getDate()));
            ps.setTime(2, Time.valueOf(reservation.getTime()));
            ps.setString(3, reservation.getName());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findByDate(String date) {
        String sql = "SELECT date, time, name from reservation where date = ?;";
        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> new Reservation(
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getTime("time").toLocalTime(),
                        resultSet.getString("name")
                ), Date.valueOf(date));
    }

    public Reservation findByDateAndTime(String date, String time) {
        String sql = "SELECT date, time, name from reservation where date = ? and time = ?;";
        try {
            return jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> new Reservation(
                            resultSet.getDate("date").toLocalDate(),
                            resultSet.getTime("time").toLocalTime(),
                            resultSet.getString("name")
                    ), Date.valueOf(date), Time.valueOf(time + ":00"));
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteByDateAndTime(String date, String time) {
        jdbcTemplate.update("DELETE FROM reservation where date = ? and time = ?;", Date.valueOf(date), Time.valueOf(time + ":00"));
    }
}

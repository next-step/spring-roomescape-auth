package nextstep.reservation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Component
public class ReservationDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Reservation reservation) {
        String sql = "INSERT INTO reservation (date, time, name) VALUES (?, ?, ?);";
        jdbcTemplate.update(sql, Date.valueOf(reservation.getDate()), Time.valueOf(reservation.getTime()), reservation.getName());
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

    public void deleteByDateAndTime(String date, String time) {
        jdbcTemplate.update("DELETE FROM reservation where date = ? and time = ?;", Date.valueOf(date), Time.valueOf(time + ":00"));
    }
}

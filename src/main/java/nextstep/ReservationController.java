package nextstep;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
public class ReservationController {
    public final JdbcTemplate jdbcTemplate;

    public ReservationController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/reservations")
    public ResponseEntity addReservation(@RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation(
                LocalDate.parse(reservationRequest.getDate()),
                LocalTime.parse(reservationRequest.getTime() + ":00"),
                reservationRequest.getName()
        );

        String sql = "INSERT INTO reservation (date, time, name) VALUES (?, ?, ?);";
        jdbcTemplate.update(sql, Date.valueOf(reservation.getDate()), Time.valueOf(reservation.getTime()), reservation.getName());


        return ResponseEntity.ok().build();
    }

    @GetMapping("/reservations")
    public ResponseEntity showReservations(@RequestParam String date) {
        String sql = "SELECT date, time, name from reservation where date = ?;";
        List<Reservation> result = jdbcTemplate.query(sql,
                (resultSet, rowNum) -> new Reservation(
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getTime("time").toLocalTime(),
                        resultSet.getString("name")
                ), Date.valueOf(date));

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/reservations")
    public ResponseEntity deleteReservation(@RequestParam String date, @RequestParam String time) {
        jdbcTemplate.update("DELETE FROM reservation where date = ? and time = ?;", Date.valueOf(date), Time.valueOf(time + ":00"));

        return ResponseEntity.noContent().build();
    }
}

package nextstep.infra.h2;

import nextstep.core.reservation.Reservation;
import nextstep.core.reservation.out.ReservationRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationH2Repository implements ReservationRepository {
    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getLong("schedule_id"),
            resultSet.getDate("date").toLocalDate(),
            resultSet.getTime("time").toLocalTime(),
            resultSet.getString("name")
    );
    private final JdbcTemplate template;

    public ReservationH2Repository(JdbcTemplate template) {
        this.template = template;
    }


    @Override
    public Reservation save(Reservation reservation) {
        Objects.requireNonNull(reservation);

        String query = "INSERT INTO reservation(schedule_id, date, time, name) VALUES (?, ?, ?, ?)";
        template.update(query, reservation.getScheduleId(), reservation.getDate(), reservation.getTime(), reservation.getName());

        Long id = template.queryForObject("SELECT last_insert_id()", Long.class);
        return new Reservation(id, reservation.getScheduleId(), reservation.getDate(), reservation.getTime(), reservation.getName());
    }

    @Override
    public List<Reservation> findAllByScheduleIdAndDate(Long scheduleId, LocalDate date) {
        Objects.requireNonNull(scheduleId);
        Objects.requireNonNull(date);

        String query = "SELECT * FROM reservation WHERE schedule_id = ? AND date = ?";
        return template.query(query, ROW_MAPPER, scheduleId, date);
    }

    @Override
    public void deleteByDateAndTime(Long scheduleId, LocalDate date, LocalTime time) {
        Objects.requireNonNull(scheduleId);
        Objects.requireNonNull(date);
        Objects.requireNonNull(time);

        String query = "DELETE FROM reservation WHERE schedule_id = ? AND date = ? AND time = ?";
        template.update(query, scheduleId, date, time);
    }

    @Override
    public boolean existsById(Long scheduleId, LocalDate date, LocalTime time) {
        Objects.requireNonNull(scheduleId);
        Objects.requireNonNull(date);
        Objects.requireNonNull(time);

        String query = "SELECT reservation.id FROM reservation WHERE schedule_id = ? AND date = ? AND time = ?";
        try {
            return Boolean.TRUE.equals(template.queryForObject(query, Boolean.class, scheduleId, date, time));
        } catch (EmptyResultDataAccessException e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public void deleteById(Long reservationId) {
        Objects.requireNonNull(reservationId);

        String query = "DELETE FROM reservation WHERE id = ?";
        template.update(query, reservationId);
    }

    @Override
    public boolean existsById(Long reservationId) {
        Objects.requireNonNull(reservationId);

        String query = "SELECT reservation.id FROM reservation WHERE id = ?";
        try {
            return Boolean.TRUE.equals(template.queryForObject(query, Boolean.class, reservationId));
        } catch (EmptyResultDataAccessException e) {
            return Boolean.FALSE;
        }
    }
}

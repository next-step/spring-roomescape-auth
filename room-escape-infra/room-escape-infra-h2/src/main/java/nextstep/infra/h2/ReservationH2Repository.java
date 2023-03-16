package nextstep.infra.h2;

import nextstep.core.reservation.Reservation;
import nextstep.core.reservation.out.ReservationRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationH2Repository implements ReservationRepository {
    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getLong("schedule_id"),
            resultSet.getLong("member_id")
    );
    private final JdbcTemplate template;

    public ReservationH2Repository(JdbcTemplate template) {
        this.template = template;
    }


    @Override
    public Reservation save(Reservation reservation) {
        Objects.requireNonNull(reservation);

        String query = "INSERT INTO reservation(schedule_id, member_id) VALUES (?, ?)";
        template.update(query, reservation.getScheduleId(), reservation.getMemberId());

        Long id = template.queryForObject("SELECT last_insert_id()", Long.class);
        return new Reservation(id, reservation.getScheduleId(), reservation.getMemberId());
    }

    @Override
    public List<Reservation> findAllByScheduleIdAndDate(Long scheduleId, LocalDate date) {
        Objects.requireNonNull(scheduleId);
        Objects.requireNonNull(date);

        String query = "SELECT * FROM reservation INNER JOIN schedules on reservation.schedule_id = schedules.id INNER JOIN themes on schedules.theme_id = theme_id WHERE schedule_id = ? AND schedules.date = ?";
        return template.query(query, ROW_MAPPER, scheduleId, date);
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

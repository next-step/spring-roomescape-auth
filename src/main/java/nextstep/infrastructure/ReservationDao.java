package nextstep.infrastructure;

import java.util.Optional;
import nextstep.domain.Reservation;
import nextstep.domain.repository.ReservationRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> reservation = (rs, rowNum) -> new Reservation(
        rs.getLong("id"),
        rs.getLong("scheduleId"),
        rs.getLong("memberId")
    );

    @Override
    public void save(Reservation reservation) {
        jdbcTemplate.update(
            "insert into reservation (scheduleId, memberId) values (?, ?)",
            reservation.getScheduleId(),
            reservation.getMemberId()
        );
    }

    @Override
    public Optional<Reservation> findBy(Long scheduleId, Long memberId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                "select id, scheduleId, memberId from reservation where scheduleId = ? and memberId = ?",
                reservation,
                scheduleId,
                memberId
            ));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteBy(Long id) {
        jdbcTemplate.update("delete from reservation where id = ?", id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from reservation");
    }
}

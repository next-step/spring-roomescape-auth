package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(reservationTime);

        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return reservationTime.toEntity(reservationTime, id);
    }

    public List<ReservationTime> findAll() {
        final String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new ReservationTime(
                        rs.getLong("id")
                        , rs.getString("start_at")
                )
        );
    }

    public void delete(Long id) {
        final String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<ReservationTime> findById(Long id) {
        final String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";

        ReservationTime reservationTime;
        try {
            reservationTime = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> new ReservationTime(
                            rs.getLong("id")
                            , rs.getString("start_at"))
                    , id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return Optional.ofNullable(reservationTime);
    }

    public Long findByStartAt(String startAt) {
        final String sql = "SELECT count(*) FROM reservation_time WHERE start_at = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, startAt);
    }

    public List<ReservationTime> findAllByAvailableTime(String date, Long themeId) {
        final String sql = """
                 SELECT rt.id, rt.start_at
                 FROM reservation_time rt
                 WHERE NOT EXISTS (
                     SELECT 1
                     FROM reservation r
                     WHERE r.reservation_date = ?
                     AND r.theme_id = ?
                     AND r.time_id = rt.id
                 )
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ReservationTime(
                rs.getLong("id"),
                rs.getString("start_at")
        ), date, themeId);
    }
}

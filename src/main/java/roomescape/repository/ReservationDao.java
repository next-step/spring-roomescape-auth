package roomescape.repository;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("reservation_date", reservation.getReservationDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return reservation.toEntity(reservation, id);
    }

    public List<Reservation> findAll() {
        final String sql = """
                SELECT 
                r.id as reservation_id
                , r.name as reservation_name
                , r.reservation_date as reservation_date
                , t.id as time_id
                , t.start_at as time_start_at
                , theme.id as theme_id
                , theme.name as theme_name
                , theme.description as theme_description
                , theme.thumbnail as theme_thumbnail
                 FROM reservation as r
                 INNER JOIN reservation_time as t ON r.time_id = t.id
                 INNER JOIN theme ON r.theme_id = theme.id
                 """;

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            ReservationTime reservationTime = new ReservationTime(
                    resultSet.getLong("time_id")
                    , resultSet.getString("time_start_at"));

            ReservationTheme reservationTheme = new ReservationTheme(
                    resultSet.getLong("theme_id")
                    , resultSet.getString("theme_name")
                    , resultSet.getString("theme_description")
                    , resultSet.getString("theme_thumbnail")
            );

            return new Reservation(resultSet.getLong("id")
                    , resultSet.getString("name")
                    , resultSet.getString("reservation_date")
                    , reservationTime
                    , reservationTheme);
        });
    }

    public void delete(Long id) {
        final String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Long countByDateAndTimeIdAndThemeId(String date, Long timeId, Long themeId) {
        final String sql = """
                    SELECT count(*) 
                    FROM reservation r 
                    WHERE reservation_date = ? 
                    AND time_id = ?
                    AND theme_id = ?
                """;
        return jdbcTemplate.queryForObject(sql, Long.class, date, timeId, themeId);
    }

    public Long countByTimeId(Long timeId) {
        final String sql = "SELECT count(*) FROM reservation WHERE time_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, timeId);
    }

    public Long countByThemeId(Long themeId) {
        final String sql = "SELECT count(*) FROM reservation r WHERE theme_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, themeId);
    }
}


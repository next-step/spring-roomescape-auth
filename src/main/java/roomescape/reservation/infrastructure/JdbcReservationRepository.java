package roomescape.reservation.infrastructure;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.user.domain.User;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource paramsReservation = new MapSqlParameterSource()
                .addValue("date", reservation.getDate())
                .addValue("user_id", reservation.getUser().getId())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Long reservationId = simpleJdbcInsert.executeAndReturnKey(paramsReservation).longValue();
        return new Reservation(reservationId, reservation.getDate(), reservation.getUser(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id , r.date, t.id as time_id, t.start_at,
                th.id as theme_id, th.name as theme_name, th.description, th.thumbnail,
                u.id as user_id, u.name as user_name, u.email, u.password, u.role
                FROM reservation as r
                INNER JOIN reservation_time as t
                ON r.time_id = t.id
                INNER JOIN theme as th
                ON r.theme_id = th.id
                INNER JOIN users as u
                ON r.user_id = u.id
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("date"),
                new User(
                        resultSet.getLong("user_id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role")
                ),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getString("start_at")
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                )
        ));
    }

    @Override
    public List<Reservation> findAllByUserIdAndThemeIdAndDateBetween(Long userId, Long themeId, LocalDate dateFrom, LocalDate DateTo) {
        String sql = """
                SELECT r.id , r.date, t.id as time_id, t.start_at,
                th.id as theme_id, th.name as theme_name, th.description, th.thumbnail,
                u.id as user_id, u.name as user_name, u.email, u.password, u.role
                FROM reservation as r
                INNER JOIN reservation_time as t
                ON r.time_id = t.id
                INNER JOIN theme as th
                ON r.theme_id = th.id
                INNER JOIN users as u
                ON r.user_id = u.id
                WHERE r.user_id = ? AND r.theme_id = ? AND r.date BETWEEN ? AND ?
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("date"),
                new User(
                        resultSet.getLong("user_id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role")
                ),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getString("start_at")
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                )
        ), userId, themeId, dateFrom, DateTo);
    }

    @Override
    public void deleteById(Long reservationId) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, reservationId);
    }

    @Override
    public boolean existsById(Long reservationId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, reservationId);
        return count != null && count.equals(1);
    }

    @Override
    public boolean existsByReservationTimeId(Long reservationTimeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, reservationTimeId);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByDateAndTimeId(String date, Long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE date = ? AND time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId);
        return count != null && count.equals(1);
    }

    @Override
    public boolean existsByDateAndReservationTimeAndTheme(String date, ReservationTime reservationTime, Theme theme) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, reservationTime.getId(), theme.getId());
        return count != null && count.equals(1);
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE theme_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, themeId);
        return count != null && count.equals(1);
    }
}

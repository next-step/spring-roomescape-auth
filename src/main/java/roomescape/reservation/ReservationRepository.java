package roomescape.reservation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservationTheme.ReservationTheme;
import roomescape.reservationTime.ReservationTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper;
    private final RowMapper<ReservationTime> reservationTimeRowMapper;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        this.reservationRowMapper = (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("reservation_name"),
                resultSet.getString("reservation_date"),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getString("start_at")
                ),
                new ReservationTheme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail")
                )
        );
        this.reservationTimeRowMapper = (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("reservation_time_id"),
                resultSet.getString("start_at")
        );

    }

    public List<Reservation> findAll() {
        final String sql = """
                                SELECT r.id as reservation_id, 
                                       r.name as reservation_name, 
                                       r.date as reservation_date, 
                                       t.id as time_id, 
                                       t.start_at as start_at,
                                       th.id as theme_id,
                                       th.name as theme_name,      
                                       th.description as theme_description,      
                                       th.thumbnail as theme_thumbnail      
                                FROM reservation as r 
                                inner join reservation_time as t on r.time_id = t.id
                                inner join theme as th on r.theme_id = th.id
                            """;

        return jdbcTemplate.query(sql, reservationRowMapper);

    }

    public Long save(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getReservationTime().getId());
        parameters.put("theme_id", reservation.getReservationTheme().getId());
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public void deleteById(Long id) {
        final String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, Long.valueOf(id));
    }


    public Reservation findById(Long id) {
        final String sql = """
                SELECT r.id as reservation_id, 
                        r.name as reservation_name, 
                        r.date as reservation_date, 
                        t.id as time_id, 
                        t.start_at as start_at,
                        th.id  as theme_id,
                        th.name as theme_name,
                        th.description as theme_description,
                        th.thumbnail as theme_thumbnail
                FROM reservation as r 
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                where r.id = ?
                """;
        final Reservation reservation = jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
        return reservation;

    }

    public boolean existsById(Long id) {
        final String sql = "select exists(select 1 from reservation where id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    public List<ReservationTime> getAvailableReservationTimes(String date, Long themeId) {
        String sql = """
                SELECT rt.id as reservation_time_id, rt.start_at as start_at
                FROM reservation_time rt
                WHERE rt.id NOT IN (
                    SELECT r.time_id
                    FROM reservation r
                    WHERE r.date = ? AND r.theme_id = ?
                )
                """;

        return jdbcTemplate.query(sql, reservationTimeRowMapper, date, themeId);
    }


}

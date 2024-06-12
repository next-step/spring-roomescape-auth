package roomescape.apply.reservationtime.domain.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.apply.reservationtime.domain.ReservationTime;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Repository
public class ReservationTimeJDBCRepository implements ReservationTimeRepository {

    private static final String INSERT_SQL = """
                INSERT INTO reservation_time(start_at)
                 VALUES (:start_at)
            """;

    private static final String SELECT_ALL_SQL = """
                SELECT
                    id,
                    start_at
                FROM
                    reservation_time
            """;

    private static final String CHECK_ID_EXISTS_SQL = """
                SELECT
                    id
                FROM
                    reservation_time
                WHERE
                    id = :id
            """;

    private static final String SELECT_ONE_SQL = """
                SELECT
                    id,
                    start_at
                FROM
                    reservation_time
                WHERE
                    id = :timeId
            """;

    private static final String DELETE_SQL = """
                DELETE FROM reservation_time
                WHERE id = :id
            """;

    private static final String SELECT_RESERVED_TIMES_SQL = """
            SELECT
                rt.id as time_id
            FROM
                reservation_time rt
                INNER JOIN reservation r ON rt.id = r.time_id
            WHERE
                r.date = :date
                AND r.theme_id = :themeId
            """;

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public ReservationTimeJDBCRepository(JdbcTemplate template) {
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(template);
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("start_at", reservationTime.getStartAt());

        namedJdbcTemplate.update(INSERT_SQL, parameters, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        reservationTime.changeId(id);

        return reservationTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return namedJdbcTemplate.query(SELECT_ALL_SQL, reservationTimeRowMapper());
    }

    @Override
    public void deleteById(Long id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        namedJdbcTemplate.update(DELETE_SQL, parameters);
    }

    @Override
    public Optional<Long> checkIdExists(long id) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("id", id);
            Long reservation = namedJdbcTemplate.queryForObject(CHECK_ID_EXISTS_SQL, parameters, Long.class);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReservationTime> findById(long timeId) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("timeId", timeId);
            ReservationTime reservationTime = namedJdbcTemplate.queryForObject(SELECT_ONE_SQL,
                    parameters,
                    reservationTimeRowMapper());
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Long> findReservedTimeIds(String date, long themeId) {
        MapSqlParameterSource reservedParam = new MapSqlParameterSource();
        reservedParam.addValue("date", date);
        reservedParam.addValue("themeId", themeId);
        return namedJdbcTemplate.queryForList(SELECT_RESERVED_TIMES_SQL, reservedParam, Long.class);
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (rs, rowNum) -> new ReservationTime(rs.getLong("id"), rs.getString("start_at"));
    }
}
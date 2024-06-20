package roomescape.apply.reservation.domain.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.theme.domain.Theme;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReservationJDBCRepository implements ReservationRepository {

    private static final String INSERT_SQL = """
                INSERT INTO reservation (name, date, time_id, theme_id, member_id)
                VALUES (:name, :date, :time_id, :theme_id, :member_id)
            """;

    private static final String SELECT_ALL_SQL = """
                SELECT
                    r.id as reservation_id,
                    r.name as reservation_name,
                    r.date as reservation_date,
                    r.member_id as member_id,
                    rt.id as time_id,
                    rt.start_at as time_start_at,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                FROM reservation as r
                inner join reservation_time as rt
                    on r.time_id = rt.id
                inner join theme as th
                    on r.theme_id = th.id
            """;

    private static final String FIND_ID_BY_ID_SQL = """
                SELECT
                    id
                FROM
                    reservation
                WHERE
                    id = :id
                LIMIT 1
            """;

    private static final String DELETE_SQL = """
                DELETE FROM reservation
                WHERE id = :id
            """;

    private static final String SELECT_ID_WHERE_TIME_ID_AND_THEME_ID_SQL = """
                SELECT
                    id
                FROM
                    reservation
                WHERE
                    time_id = :time_id
                    AND theme_id = :theme_id
            """;

    private static final String SELECT_ID_BY_TIME_ID_SQL = """
                SELECT
                    id
                FROM
                    reservation
                WHERE
                    time_id = :time_id
                LIMIT 1
            """;

    private static final String SELECT_ID_BY_THEME_ID_SQL = """
                SELECT
                    id
                FROM
                    reservation
                WHERE
                    theme_id = :theme_id
                LIMIT 1
            """;

    private final JdbcTemplate template;
    private final NamedParameterJdbcTemplate jdbcTemplate;


    public ReservationJDBCRepository(JdbcTemplate template) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(template);
    }

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", reservation.getName());
        parameters.addValue("date", reservation.getDate());
        parameters.addValue("time_id", reservation.getTime().getId());
        parameters.addValue("theme_id", reservation.getTheme().getId());
        parameters.addValue("member_id", reservation.getMemberId());

        jdbcTemplate.update(INSERT_SQL, parameters, keyHolder);

        long key = Objects.requireNonNull(keyHolder.getKey()).longValue();
        reservation.changeId(key);

        return reservation;
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, reservationRowMapper());
    }

    @Override
    public Optional<Long> findIdById(long id) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("id", id);
            Long reservation = jdbcTemplate.queryForObject(FIND_ID_BY_ID_SQL, parameters, Long.class);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(long id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        jdbcTemplate.update(DELETE_SQL, parameters);
    }

    @Override
    public Optional<Long> findIdByTimeIdAndThemeId(long timeId, long themeId) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("time_id", timeId);
            parameters.addValue("theme_id", themeId);
            Long reservationId = jdbcTemplate.queryForObject(SELECT_ID_WHERE_TIME_ID_AND_THEME_ID_SQL,
                    parameters,
                    Long.class);
            return Optional.ofNullable(reservationId);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Long> findIdByTimeId(long timeId) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("time_id", timeId);
            Long foundReservationTimeId = jdbcTemplate.queryForObject(SELECT_ID_BY_TIME_ID_SQL, parameters, Long.class);
            return Optional.ofNullable(foundReservationTimeId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Long> findIdByThemeId(long themeId) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("theme_id", themeId);
            Long foundThemeId = jdbcTemplate.queryForObject(SELECT_ID_BY_THEME_ID_SQL, parameters, Long.class);
            return Optional.ofNullable(foundThemeId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (rs, rowNum) -> {
            long timeId = rs.getLong("time_id");
            String timeStartAt = rs.getString("time_start_at");
            ReservationTime reservationTime = new ReservationTime(timeId, timeStartAt);
            long themeId = rs.getLong("theme_id");
            String themeName = rs.getString("theme_name");
            String themeDescription = rs.getString("theme_description");
            String themeThumbnail = rs.getString("theme_thumbnail");
            Theme theme = new Theme(themeId, themeName, themeDescription, themeThumbnail);
            long memberId = rs.getLong("member_id");

            return new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getString("reservation_name"),
                    rs.getString("reservation_date"),
                    reservationTime,
                    theme,
                    memberId
            );
        };
    }
}

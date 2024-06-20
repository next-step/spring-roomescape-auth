package roomescape.domain.time.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.domain.Time;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class TimeRepository {

    private static final String FIND_BY_THEME_ID_AND_DATE_SQL = """
            SELECT rt.id, rt.start_at
            FROM reservation_time rt
            WHERE NOT EXISTS (
                SELECT 1
                FROM reservation r
                WHERE r.date = ?
                AND r.theme_id = ?
                AND r.time_id = rt.id
            )
            """;
    private static final String SAVE_SQL = "INSERT INTO reservation_time (start_at) VALUES (?);";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM reservation_time WHERE id = ?;";
    private static final String FIND_ALL_SQL = "SELECT * FROM reservation_time";
    private static final String DELETE_SQL = "DELETE FROM reservation_time WHERE id = ?;";
    private static final String ID = "id";
    private static final String START_AT = "start_at";

    private final JdbcTemplate jdbcTemplate;

    public TimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, new String[]{ID});
            preparedStatement.setString(1, time.getStartAt());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Time findById(Long timeId) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, (rs, rowNum) ->
                new Time(
                        rs.getLong(ID),
                        rs.getString(START_AT)
                ), timeId);
    }

    public List<Time> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, (rs, rowNum) ->
                new Time(
                        rs.getLong(ID),
                        rs.getString(START_AT)
                ));
    }

    public void delete(Long timeId) {
        jdbcTemplate.update(DELETE_SQL, timeId);
    }

    public List<Time> findByThemeIdAndDate(String themeId, String date) {
        return jdbcTemplate.query(FIND_BY_THEME_ID_AND_DATE_SQL, (rs, rowNum) ->
                new Time(
                        rs.getLong(ID),
                        rs.getString(START_AT)
                ), date, themeId);
    }
}

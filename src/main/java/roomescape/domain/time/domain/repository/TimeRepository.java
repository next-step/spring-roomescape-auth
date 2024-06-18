package roomescape.domain.time.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class TimeRepository {

    private static final String SAVE_SQL = """
            INSERT INTO reservation_time (start_at, date, theme_id) 
            VALUES (?, ?, ?);
            """;
    private static final String FIND_BY_DATE_AND_THEME_ID_SQL = """
            SELECT
                rt.id AS reservation_time_id,
                rt.start_at AS start_time,
                rt.date AS reservation_date,
                rt.theme_id AS theme_id,
                t.name AS theme_name,
                t.description AS theme_description,
                t.thumbnail AS theme_thumbnail
            FROM reservation_time rt
            INNER JOIN theme t
                ON rt.theme_id = t.id
            WHERE
                rt.date = ? AND 
                rt.theme_id = ?;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT
                rt.id AS reservation_time_id,
                rt.start_at AS start_time,
                rt.date AS reservation_date,
                rt.theme_id AS theme_id,
                t.name AS theme_name,
                t.description AS theme_description,
                t.thumbnail AS theme_thumbnail
            FROM reservation_time rt
            INNER JOIN theme t
                ON rt.theme_id = t.id
            WHERE
                rt.id = ?;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT 
                 rt.id, 
                 rt.start_at, 
                 rt.date, 
                 rt.theme_id, 
                 t.name, 
                 t.description, 
                 t.thumbnail
            FROM reservation_time rt
            INNER JOIN theme t 
                ON rt.theme_id = t.id;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM reservation_time WHERE id = ?;
            """;
    private static final String ID = "id";
    private static final String TIME_ID = "reservation_time_id";
    private static final String TIME_START_AT = "start_time";
    private static final String TIME_DATE = "reservation_date";
    private static final String THEME_ID = "theme_id";
    private static final String THEME_NAME = "theme_name";
    private static final String THEME_DESCRIPTION = "theme_description";
    private static final String THEME_THUMBNAIL = "theme_thumbnail";

    private final JdbcTemplate jdbcTemplate;

    public TimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, new String[]{ID});
            preparedStatement.setString(1, time.getStartAt());
            preparedStatement.setString(2, time.getDate());
            preparedStatement.setLong(3, time.getTheme().getId());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Time findById(Long timeId) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, (rs, rowNum) ->
                new Time(
                        rs.getLong(TIME_ID),
                        rs.getString(TIME_DATE),
                        new Theme(
                                rs.getLong(THEME_ID),
                                rs.getString(THEME_NAME),
                                rs.getString(THEME_DESCRIPTION),
                                rs.getString(THEME_THUMBNAIL)),
                        rs.getString(TIME_START_AT)
                ), timeId);
    }

    public List<Time> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, (rs, rowNum) ->
                new Time(
                        rs.getLong(TIME_ID),
                        rs.getString(TIME_DATE),
                        new Theme(
                                rs.getLong(THEME_ID),
                                rs.getString(THEME_NAME),
                                rs.getString(THEME_DESCRIPTION),
                                rs.getString(THEME_THUMBNAIL)),
                        rs.getString(TIME_START_AT)
                ));
    }

    public void delete(Long timeId) {
        jdbcTemplate.update(DELETE_SQL, timeId);
    }

    public List<Time> findByDateAndThemeId(String date, String themeId) {
        return jdbcTemplate.query(FIND_BY_DATE_AND_THEME_ID_SQL, (rs, rowNum) ->
                new Time(
                        rs.getLong(TIME_ID),
                        rs.getString(TIME_DATE),
                        new Theme(
                                rs.getLong(THEME_ID),
                                rs.getString(THEME_NAME),
                                rs.getString(THEME_DESCRIPTION),
                                rs.getString(THEME_THUMBNAIL)),
                        rs.getString(TIME_START_AT)
                ), date, themeId);
    }
}

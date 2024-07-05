package roomescape.repository.domain.reservationtime.mysql;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import roomescape.repository.domain.reservationtime.ReservationTimeJdbcRepository;
import roomescape.repository.domain.reservationtime.entity.ReservationTimeEntity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class MySQLJdbcReservationTimeRepository implements ReservationTimeJdbcRepository {

    private static final String TABLE_COLUMN_ID = "id";
    private static final String TABLE_COLUMN_IDS = "ids";
    private static final String TABLE_COLUMN_START_AT = "start_at";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MySQLJdbcReservationTimeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public ReservationTimeEntity save(ReservationTimeEntity entity) {
        String sql = "INSERT INTO reservation_time (id, start_at) VALUES (:id, :start_at) " +
                "ON DUPLICATE KEY UPDATE start_at = VALUES(start_at)";

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(TABLE_COLUMN_ID, entity.getId())
                .addValue(TABLE_COLUMN_START_AT, entity.getStartAt());

        namedParameterJdbcTemplate.update(sql, sqlParameterSource, generatedKeyHolder);

        if (Objects.isNull(generatedKeyHolder.getKey())) {
            return entity;
        }

        return entity.withId(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public Optional<ReservationTimeEntity> findById(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = :id";

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(TABLE_COLUMN_ID, id);

        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(
                            sql,
                            sqlParameterSource,
                            (resultSet, rowNum) -> new ReservationTimeEntity(
                                    resultSet.getLong(TABLE_COLUMN_ID),
                                    resultSet.getTime(TABLE_COLUMN_START_AT).toLocalTime()
                            )
                    )
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReservationTimeEntity> findByStartAt(LocalTime startAt) {
        String sql = "SELECT id, start_at FROM reservation_time " +
                "WHERE start_at = :start_at";

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(TABLE_COLUMN_START_AT, startAt);

        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(
                            sql,
                            sqlParameterSource,
                            (resultSet, rowNum) -> new ReservationTimeEntity(
                                    resultSet.getLong(TABLE_COLUMN_ID),
                                    resultSet.getTime(TABLE_COLUMN_START_AT).toLocalTime()
                            )
                    )
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTimeEntity> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";

        return namedParameterJdbcTemplate.query(sql, resultSet -> {
            List<ReservationTimeEntity> reservationTimeEntities = new ArrayList<>();
            while (resultSet.next()) {
                reservationTimeEntities.add(
                        new ReservationTimeEntity(
                                resultSet.getLong(TABLE_COLUMN_ID),
                                resultSet.getTime(TABLE_COLUMN_START_AT).toLocalTime()
                        )
                );
            }
            return reservationTimeEntities;
        });
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = :id";

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(TABLE_COLUMN_ID, id);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public List<ReservationTimeEntity> findExcludeById(List<Long> ids) {
        String sql = "SELECT id, start_at FROM reservation_time";
        String condition = " WHERE id NOT IN (:ids)";

        if (!ids.isEmpty()) {
            sql = sql + condition;
        }

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(TABLE_COLUMN_IDS, ids);

        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, resultSet -> {
            List<ReservationTimeEntity> reservationTimeEntities = new ArrayList<>();
            while (resultSet.next()) {
                reservationTimeEntities.add(
                        new ReservationTimeEntity(
                                resultSet.getLong(TABLE_COLUMN_ID),
                                resultSet.getTime(TABLE_COLUMN_START_AT).toLocalTime()
                        )
                );
            }
            return reservationTimeEntities;
        });
    }
}

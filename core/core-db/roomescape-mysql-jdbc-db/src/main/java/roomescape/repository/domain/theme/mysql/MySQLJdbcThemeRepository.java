package roomescape.repository.domain.theme.mysql;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import roomescape.repository.domain.theme.ThemeJdbcRepository;
import roomescape.repository.domain.theme.entity.ThemeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class MySQLJdbcThemeRepository implements ThemeJdbcRepository {

    private static final String TABLE_COLUMN_ID = "id";
    private static final String TABLE_COLUMN_NAME = "name";
    private static final String TABLE_COLUMN_DESCRIPTION = "description";
    private static final String TABLE_COLUMN_THUMBNAIL = "thumbnail";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MySQLJdbcThemeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public ThemeEntity save(ThemeEntity entity) {
        String sql = "INSERT INTO theme (id, name, description, thumbnail) VALUES (:id, :name, :description, :thumbnail) " +
                "ON DUPLICATE KEY UPDATE name = VALUES(name), description = VALUES(description), thumbnail = VALUES(thumbnail)";

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(TABLE_COLUMN_ID, entity.getId())
                .addValue(TABLE_COLUMN_NAME, entity.getName())
                .addValue(TABLE_COLUMN_DESCRIPTION, entity.getDescription())
                .addValue(TABLE_COLUMN_THUMBNAIL, entity.getThumbnail());

        namedParameterJdbcTemplate.update(sql, sqlParameterSource, generatedKeyHolder);

        if (Objects.isNull(generatedKeyHolder.getKey())) {
            return entity;
        }

        return entity.withId(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public Optional<ThemeEntity> findById(Long id) {
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = :id";

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(TABLE_COLUMN_ID, id);

        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(
                            sql,
                            sqlParameterSource,
                            (resultSet, rowNum) -> new ThemeEntity(
                                    resultSet.getLong(TABLE_COLUMN_ID),
                                    resultSet.getString(TABLE_COLUMN_NAME),
                                    resultSet.getString(TABLE_COLUMN_DESCRIPTION),
                                    resultSet.getString(TABLE_COLUMN_THUMBNAIL)
                            )
                    )
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<ThemeEntity> findAll() {
        String sql = "SELECT id, name, description, thumbnail FROM theme";

        return namedParameterJdbcTemplate.query(sql, resultSet -> {
            List<ThemeEntity> themeEntities = new ArrayList<>();
            while (resultSet.next()) {
                themeEntities.add(
                        new ThemeEntity(
                                resultSet.getLong(TABLE_COLUMN_ID),
                                resultSet.getString(TABLE_COLUMN_NAME),
                                resultSet.getString(TABLE_COLUMN_DESCRIPTION),
                                resultSet.getString(TABLE_COLUMN_THUMBNAIL)
                        )
                );
            }
            return themeEntities;
        });
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM theme WHERE id = :id";

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(TABLE_COLUMN_ID, id);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }
}

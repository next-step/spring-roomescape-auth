package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTheme;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationThemeDao(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTheme save(ReservationTheme reservationTheme) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(reservationTheme);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return reservationTheme.toEntity(reservationTheme, id);
    }

    public List<ReservationTheme> findAll() {
        final String sql = "SELECT id, name, description, thumbnail FROM theme";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ReservationTheme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("thumbnail")
        ));
    }

    public void delete(Long id) {
        final String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<ReservationTheme> findById(Long id) {
        final String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";

        ReservationTheme reservationTheme;
        try {
            reservationTheme = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> new ReservationTheme(
                            rs.getLong("id")
                            , rs.getString("name")
                            , rs.getString("description")
                            , rs.getString("thumbnail")
                    ), id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return Optional.ofNullable(reservationTheme);
    }

    public Long findByName(String name) {
        final String sql = "SELECT count(*) FROM theme WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, name);
    }
}


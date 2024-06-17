package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTheme;

@Repository
public class JdbcReservationThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ReservationTheme save(ReservationTheme reservationTheme) {
        final String sql = "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservationTheme.getName());
            ps.setString(2, reservationTheme.getDescription());
            ps.setString(3, reservationTheme.getThumbnail());

            return ps;
        }, keyHolder);

        return reservationTheme.toEntity(reservationTheme, keyHolder.getKey().longValue());
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

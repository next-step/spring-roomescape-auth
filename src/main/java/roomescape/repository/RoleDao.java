package roomescape.repository;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.Role;
import roomescape.domain.RoleType;

@Repository
public class RoleDao {
    private final JdbcTemplate jdbcTemplate;

    public RoleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Role> findByName(String name) {
        final String sql = "SELECT id, name FROM role WHERE name = ?";

        Role role;
        try {
            role = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> new Role(
                            rs.getLong("id")
                            , RoleType.fromName(rs.getString("name")))
                    , name);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return Optional.ofNullable(role);
    }
}

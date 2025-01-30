package roomescape.domain.user.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomescape.domain.common.exception.DataAccessException;
import roomescape.domain.user.domain.User;
import roomescape.domain.user.domain.UserRole;

import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJdbcRepository {

    private static final String SELECT_ALL_USER_SQL = """
            select
                user_id,
                role,
                name,
                email,
                password
            from users""";

    private static final RowMapper<User> USER_ENTITY_ROW_MAPPER =
            (rs, rowNum) -> User.builder()
                    .id(rs.getLong("user_id"))
                    .role(UserRole.valueOf(rs.getString("role")))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .build();

    private final JdbcTemplate jdbcTemplate;

    public Optional<User> findByEmail(final String email) {
        if (Objects.isNull(email)) {
            return Optional.empty();
        }
        return queryForTheme(SELECT_ALL_USER_SQL + " where email = ? ", email);
    }

    private Optional<User> queryForTheme(final String selectSql, Object... objects) {
        try {
            final User user = jdbcTemplate.queryForObject(
                    selectSql,
                    USER_ENTITY_ROW_MAPPER,
                    objects
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public User save(final User user) {
        if (Objects.isNull(user.getId())) {
            return insertWithKeyHolder(user);
        }

        updateAll(user);

        return user;
    }

    private void updateAll(final User user) {
        final String updateSql = """
                update users set
                    role = ?,
                    name = ?, 
                    email = ?,
                    password = ? 
                where user_id = ?""";

        final int updatedRowCount = jdbcTemplate.update(
                updateSql,
                user.getRole().name(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getId()
        );

        if (updatedRowCount != 1) {
            throw new DataAccessException(
                    "Error occurred while updating Theme where theme_id=%d. Affected row is not 1 but %d."
                            .formatted(user.getId(), updatedRowCount)
            );
        }
    }

    private User insertWithKeyHolder(final User user) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        final String insertSql = """
                insert into users (
                    role, 
                    name, 
                    email,
                    password
                ) values (?, ?, ?, ?)""";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"user_id"});
            ps.setString(1, user.getRole().name());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            return ps;
        }, keyHolder);

        final long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return User.builder()
                .id(generatedId)
                .role(user.getRole())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public void deleteAllInBatch() {
        jdbcTemplate.execute("delete from users");
    }
}

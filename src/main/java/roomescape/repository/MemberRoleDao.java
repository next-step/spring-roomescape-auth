package roomescape.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.RoleType;

@Repository
public class MemberRoleDao {
    private final SimpleJdbcInsert memberRoleInsert;
    private final JdbcTemplate jdbcTemplate;

    public MemberRoleDao(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberRoleInsert = new SimpleJdbcInsert(source)
                .withTableName("MEMBER_ROLE")
                .usingGeneratedKeyColumns("id");
    }

    public void save(Long memberId, Long roleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", memberId);
        params.put("role_id", roleId);
        SqlParameterSource source = new MapSqlParameterSource(params);

        memberRoleInsert.executeAndReturnKey(source).longValue();
    }

    public Optional<RoleType> findRoleByMemberId(Long memberId) {
        final String sql = """
                SELECT r.name as role_name
                FROM member m
                INNER JOIN member_role mr ON m.id = mr.member_id
                INNER JOIN role r ON r.id = mr.role_id
                where m.id = ?
                """;

        RoleType roleType;
        try {
            roleType = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> RoleType.fromName(rs.getString("role_name"))
                    , memberId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return Optional.ofNullable(roleType);
    }
}

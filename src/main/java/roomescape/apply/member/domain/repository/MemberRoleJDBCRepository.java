package roomescape.apply.member.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.apply.member.domain.MemberRole;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Repository
public class MemberRoleJDBCRepository implements MemberRoleRepository {

    private static final String INSERT_SQL = """
                INSERT INTO member_role(name, member_id)
                 VALUES (:name, :member_id)
            """;

    private static final String SELECT_ROLE_NAMES_SQL = """
                SELECT
                    name
                FROM
                    member_role
                WHERE
                    member_id = :member_id
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MemberRoleJDBCRepository(JdbcTemplate template) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(template);
    }

    public void saveAll(Iterator<MemberRole> memberRoles) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        memberRoles.forEachRemaining(it -> {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("name", it.getMemberRoleName().name());
            parameters.addValue("member_id", it.getMemberId());
            jdbcTemplate.update(INSERT_SQL, parameters, keyHolder);
            long key = Objects.requireNonNull(keyHolder.getKey()).longValue();
            it.changeId(key);
        });
    }

    @Override
    public List<String> findNamesByMemberId(Long memberId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("member_id", memberId);
        return jdbcTemplate.queryForList(SELECT_ROLE_NAMES_SQL, parameters, String.class);
    }
}

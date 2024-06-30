package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.domain.RoleType;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert memberInsert;

    public MemberDao(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberInsert = new SimpleJdbcInsert(source)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("id");
    }

    public Member save(Member member) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword())
                .addValue("role_id", member.getRole().getId());

        Long id = memberInsert.executeAndReturnKey(params).longValue();

        return member.toEntity(member, id);
    }

    public Optional<Member> findByEmail(String email) {
        final String sql = """
                SELECT 
                    m.id as member_id
                    , m.name as member_name
                    , m.email as member_email
                    , m.password as member_password
                    , r.id as role_id
                    , r.name as role_name 
                FROM member m 
                INNER JOIN role r ON m.role_id = r.id   
                WHERE m.email = ?
                """;

        Member member;
        try {
            member = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> new Member(
                            rs.getLong("member_id")
                            , rs.getString("member_name")
                            , rs.getString("member_email")
                            , rs.getString("member_password")
                            , new Role(rs.getLong("role_id")
                            , RoleType.fromName(rs.getString("role_name"))
                    )), email);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return Optional.ofNullable(member);
    }

    public Optional<Member> findById(Long id) {
        final String sql = """
                SELECT 
                    m.id as member_id
                    , m.name as member_name
                    , m.email as member_email
                    , m.password as member_password
                    , r.id as role_id
                    , r.name as role_name 
                FROM member m 
                INNER JOIN role r ON m.role_id = r.id   
                WHERE m.id = ?
                """;

        Member member;
        try {
            member = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> new Member(
                            rs.getLong("id")
                            , rs.getString("name")
                            , rs.getString("email")
                            , rs.getString("password")
                            , new Role(rs.getLong("role_id"),
                            RoleType.fromName(rs.getString("role_name"))
                    )), id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return Optional.ofNullable(member);
    }

    public List<Member> findAllMembers() {
        final String sql = "select id, name from member";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("name")));
    }
}

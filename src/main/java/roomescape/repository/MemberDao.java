package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("id");
    }

    public Member save(Member member) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return member.toEntity(member, id);
    }

    public Optional<Member> findByEmail(String email) {
        final String sql = "SELECT id, name, email, password FROM member where email = ?";

        Member member;
        try {
            member = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> new Member(
                            rs.getLong("id")
                            , rs.getString("name")
                            , rs.getString("email")
                            , rs.getString("password")
                    ), email);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return Optional.ofNullable(member);
    }

    public Optional<Member> findById(Long id) {
        final String sql = "SELECT id, name, email, password FROM member where id = ?";

        Member member;
        try {
            member = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> new Member(
                            rs.getLong("id")
                            , rs.getString("name")
                            , rs.getString("email")
                            , rs.getString("password")
                    ), id);
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

package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class JdbcMemberDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {
        final String sql = "INSERT INTO member (name, email, password) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPassword());

            return ps;
        }, keyHolder);

        return member.toEntity(member, keyHolder.getKey().longValue());
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
}

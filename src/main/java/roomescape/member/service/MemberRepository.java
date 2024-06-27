package roomescape.member.service;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.member.Member;

@Repository
public class MemberRepository {

    private static final String SELECT_STR = "SELECT id, email, password, name FROM member ";

    private static final RowMapper<Member> rowMapper = (rs, rowNumber) -> new Member(
        rs.getLong(1),
        rs.getString(2),
        rs.getString(3),
        rs.getString(4)
    );

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> find() {
        return jdbcTemplate.query(SELECT_STR, rowMapper);
    }

    public Optional<Member> findById(Long id) {
        String sql = SELECT_STR + "WHERE id = ?";

        return Optional.ofNullable(
            DataAccessUtils.singleResult(jdbcTemplate.query(sql, rowMapper, id)));
    }

    public Optional<Member> findByEmail(String email) {
        String sql = SELECT_STR + "WHERE email = ?";

        return Optional.ofNullable(
            DataAccessUtils.singleResult(jdbcTemplate.query(sql, rowMapper, email)));
    }
}

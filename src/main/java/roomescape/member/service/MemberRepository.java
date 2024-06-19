package roomescape.member.service;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.member.Member;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member findByEmail(String email) {
        String sql = "SELECT id, email, password, name FROM member WHERE email = ?";

        RowMapper<Member> rowMapper = (rs, rowNumber) -> new Member(
            rs.getLong(1),
            rs.getString(2),
            rs.getString(3),
            rs.getString(4)
        );

        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, rowMapper, email));
    }
}

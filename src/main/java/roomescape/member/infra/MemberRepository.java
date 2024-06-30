package roomescape.member.infra;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.reservationtime.domain.ReservationTime;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> rowMapper;

    public MemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role")
        );
    }

    public boolean isExistMemBerByEmailAndPassword(final String email, final String password) {
        final String sql = "SELECT EXISTS(SELECT 1 FROM member WHERE email = ? and password = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, email, password);
    }

    public Long findIdByEmail(final String email) {
        final String sql = "SELECT id FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, email);
    }

    public String findNameById(final Long id) {
        final String sql = "SELECT name FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    public Member findMemberById(final long id) {
        final String sql = "SELECT * FROm member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
}

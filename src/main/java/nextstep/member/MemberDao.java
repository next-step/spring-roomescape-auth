package nextstep.member;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class MemberDao {
    public final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> rowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getLong("password_id"),
            resultSet.getString("name"),
            resultSet.getString("phone")
    );

    public Long save(Member member) {
        String sql = "INSERT INTO member (username, password_id, name, phone) VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getUsername());
            ps.setLong(2, member.getPasswordId());
            ps.setString(3, member.getName());
            ps.setString(4, member.getPhone());
            return ps;

        }, keyHolder);

        long memberId = keyHolder.getKey().longValue();

        String roleSql = "INSERT INTO member_role (member_id, member_role) VALUES (?, ?);";
        List<MemberRole> roles = member.getRoles();
        roles.forEach(
            role -> jdbcTemplate.update(roleSql, memberId, role.name())
        );

        return memberId;
    }

    public Member findById(Long id) {
        String sql = "SELECT id, username, password_id, name, phone from member where id = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Member findByUsername(String username) {
        String sql = "SELECT id, username, password_id, name, phone from member where username = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }

    public List<MemberRole> findMemberRoleByMemberId(Long memberId) {
        String sql = "SELECT member_role FROM member_role WHERE member_id = ?;";
        List<MemberRole> roles = jdbcTemplate.query(sql, (rs, rowNum) -> MemberRole.valueOf(rs.getString("member_role")), memberId);
        return roles;
    }
}

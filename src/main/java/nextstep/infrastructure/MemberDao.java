package nextstep.infrastructure;

import java.sql.PreparedStatement;
import java.util.Optional;
import nextstep.common.Role;
import nextstep.common.exception.MemberException;
import nextstep.domain.Member;
import nextstep.domain.repository.MemberRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao implements MemberRepository {

    public final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> member = (resultSet, rowNum) -> new Member(
        resultSet.getLong("id"),
        resultSet.getString("username"),
        resultSet.getString("password"),
        resultSet.getString("name"),
        resultSet.getString("phone"),
        Role.valueOf(resultSet.getString("role"))
    );

    @Override
    public Long save(Member member) {
        String sql = "INSERT INTO member (username, password, name, phone, role) VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});

            ps.setString(1, member.getUsername());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getName());
            ps.setString(4, member.getPhone());
            ps.setString(5, member.getRole().name());

            return ps;
        }, keyHolder);

        return Optional.ofNullable(keyHolder.getKey())
            .map(Number::longValue)
            .orElseThrow(() -> new MemberException("사용자를 저장할 수 없습니다. DB를 확인해주세요."));
    }

    @Override
    public Optional<Member> findBy(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT id, username, password, name, phone, role FROM member WHERE id = ?",
                member,
                id
            ));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findBy(String username) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT id, username, password, name, phone, role FROM member WHERE username = ?",
                member,
                username
            ));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM member");
    }
}

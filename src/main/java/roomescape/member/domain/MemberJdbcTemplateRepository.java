package roomescape.member.domain;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.entity.Member;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJdbcTemplateRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> rowMapper = (resultSet, rowNum) -> Member.of(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("role"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    @Override
    public List<Member> findAll() {
        String sql = """
                SELECT *
                FROM member
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = """
                select *
                from member
                where id = ?
                """;

        try {
            Member member = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(member);
        }
        catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = """
                select *
                from member
                where email = ?
                """;

        try {
            Member member = jdbcTemplate.queryForObject(sql, rowMapper, email);
            return Optional.ofNullable(member);
        }
        catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = """
                select *
                from member
                where email = ?
                and password = ?
                """;

        try {
            Member member = jdbcTemplate.queryForObject(sql, rowMapper, email, password);
            return Optional.ofNullable(member);
        }
        catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public long save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            String sql = """
                    insert into member
                    (name, role, email, password)
                    values (?, ?, ?, ?)
                    """;
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, member.getName());
            ps.setString(2, member.getRoleName());
            ps.setString(3, member.getEmail());
            ps.setString(4, member.getPassword());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}

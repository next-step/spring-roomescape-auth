package roomescape.member.domain;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.entity.Member;

import java.sql.PreparedStatement;
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
            resultSet.getString("email"),
            resultSet.getString("password")
    );

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
    public long save(String name, String email, String password) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            String sql = """
                    insert into member
                    (name, email, password)
                    values (?, ?, ?)
                    """;
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}

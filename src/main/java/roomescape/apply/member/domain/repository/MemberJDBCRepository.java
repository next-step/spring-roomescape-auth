package roomescape.apply.member.domain.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.apply.member.domain.Member;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class MemberJDBCRepository implements MemberRepository {

    private static final String SELECT_BY_EMAIL_AND_PASSWORD_SQL = """
                SELECT
                    id,
                    name,
                    email,
                    password
                FROM
                    member
                WHERE
                    email = ?
                    AND password = ?
            """;

    private static final String SELECT_ALL_SQL = """
                SELECT
                    id,
                    name,
                    email,
                    password
                FROM
                    member
            """;

    private static final String COUNT_BY_EMAIL_SQL = """
                SELECT
                    COUNT(id)
                FROM
                    member
                WHERE
                    email = ?
            """;

    private static final String INSERT_MEMBER_SQL = """
                INSERT INTO member (name, email, password)
                VALUES (?, ?, ?)
            """;

    private final JdbcTemplate template;

    public MemberJDBCRepository(JdbcTemplate template) {
        this.template = template;
    }


    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        try {
            Member foundMember = template.queryForObject(
                    SELECT_BY_EMAIL_AND_PASSWORD_SQL,
                    memberRowMapper(),
                    email,
                    password
            );
            return Optional.ofNullable(foundMember);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        return template.query(SELECT_ALL_SQL, memberRowMapper());
    }

    @Override
    public Integer countByEmail(String email) {
        try {
            return template.queryForObject(COUNT_BY_EMAIL_SQL, Integer.class, email);
        } catch (EmptyResultDataAccessException ex) {
            return 0;
        }
    }

    @Override
    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_MEMBER_SQL, new String[]{"id"});
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPassword());
            return ps;
        }, keyHolder);

        long key = Objects.requireNonNull(keyHolder.getKey()).longValue();
        member.changeId(key);

        return member;
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")
        );
    }
}

package roomescape.domain.member.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private static final String FIND_BY_EMAIL_AND_PASSWORD_SQL = """
            SELECT * 
            FROM member m
            WHERE
                m.email = ? AND 
                m.password = ?;
            """;
    private static final String FIND_BY_ID = """
            SELECT * 
            FROM member m
            WHERE
                m.id = ?;
            """;

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> login(String email, String password) {
        List<Member> members = jdbcTemplate.query(FIND_BY_EMAIL_AND_PASSWORD_SQL, (rs, rowNum) ->
                        new Member(
                                rs.getLong("id"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("name")),
                email, password);
        if (members.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(members.get(0));
    }

    public Optional<Member> findById(Long memberId) {
        List<Member> members = jdbcTemplate.query(FIND_BY_ID, (rs, rowNum) ->
                        new Member(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password")
                        ),
                memberId);
        if (members.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(members.get(0));
    }
}

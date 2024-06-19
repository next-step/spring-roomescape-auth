package roomescape.domain.member.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private static final String SAVE_SQL = "INSERT INTO member (name, email, password) VALUES (?, ?, ?)";
    private static final String FIND_BY_EMAIL_AND_PASSWORD_SQL = """
            SELECT * 
            FROM member m
            WHERE
                m.email = ? AND 
                m.password = ?;
            """;
    private static final String FIND_BY_ID_SQL = "SELECT * FROM member m WHERE m.id = ?;";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> login(String email, String password) {
        List<Member> members = jdbcTemplate.query(FIND_BY_EMAIL_AND_PASSWORD_SQL, (rs, rowNum) ->
                        new Member(
                                rs.getLong(ID),
                                rs.getString(NAME),
                                rs.getString(EMAIL),
                                rs.getString(PASSWORD)
                        ),
                email, password);
        if (members.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(members.get(0));
    }

    public Optional<Member> findById(Long memberId) {
        List<Member> members = jdbcTemplate.query(FIND_BY_ID_SQL, (rs, rowNum) ->
                        new Member(
                                rs.getLong(ID),
                                rs.getString(NAME),
                                rs.getString(EMAIL),
                                rs.getString(PASSWORD)
                        ),
                memberId);
        if (members.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(members.get(0));
    }
}

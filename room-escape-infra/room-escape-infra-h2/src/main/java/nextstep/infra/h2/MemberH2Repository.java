package nextstep.infra.h2;

import nextstep.core.member.Member;
import nextstep.core.member.out.MemberRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
public class MemberH2Repository implements MemberRepository {
    private static final RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("phone")
    );
    private final JdbcTemplate template;


    public MemberH2Repository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Member save(Member member) {
        Objects.requireNonNull(member);

        String query = "INSERT INTO member (username, password, name, phone) VALUES (?, ?, ?, ?)";
        template.update(query, member.getUsername(), member.getPassword(), member.getName(), member.getPhone());

        Long id = template.queryForObject("SELECT last_insert_id()", Long.class);
        return new Member(id, member.getUsername(), member.getPassword(), member.getName(), member.getPhone());
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        Objects.requireNonNull(username);

        String query = "SELECT id, username, password, name, phone from member where username = ?";
        Member member = template.queryForObject(query, ROW_MAPPER, username);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        Objects.requireNonNull(memberId);

        String query = "SELECT id, username, password, name, phone from member where id = ?";
        Member member = template.queryForObject(query, ROW_MAPPER, memberId);
        return Optional.ofNullable(member);
    }
}

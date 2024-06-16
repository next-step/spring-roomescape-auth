package roomescape.adapter.out.jdbc;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.adapter.mapper.MemberMapper;
import roomescape.adapter.out.MemberEntity;
import roomescape.application.port.out.MemberPort;
import roomescape.domain.Member;

@Repository
public class MemberJdbcRepository implements MemberPort {

    private final JdbcTemplate jdbcTemplate;

    public MemberJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Member> findMembers() {
        String sql = "SELECT id, name, email FROM member";

        List<MemberEntity> memberEntities = jdbcTemplate.query(
            sql, (resultSet, rowNum) -> {
                MemberEntity memberEntity = new MemberEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    null
                );

                return memberEntity;
            }
        );

        return memberEntities.stream()
                             .map(MemberMapper::mapToDomain)
                             .toList();
    }

    @Override
    public void saveMember(Member member) {
        String sql = "INSERT INTO member(name, email, password) VALUES(?, ?, ?)";

        jdbcTemplate.update(sql, member.getName(), member.getEmail(), member.getPassword());
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM member WHERE email = ?";

        List<MemberEntity> memberEntities = jdbcTemplate.query(
            sql, new Object[] {email}, (resultSet, rowNum) -> {
                MemberEntity member = new MemberEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
                );

                return member;
            }
        );

        Member member = null;

        if (!memberEntities.isEmpty()) {
            member = MemberMapper.mapToDomain(memberEntities.get(0));
        }

        return Optional.ofNullable(member);
    }
}

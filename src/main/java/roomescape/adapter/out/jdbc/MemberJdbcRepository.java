package roomescape.adapter.out.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
        String sql = "SELECT id, name, email, role FROM member";

        List<MemberEntity> memberEntities = jdbcTemplate.query(
            sql, (resultSet, rowNum) -> {
                MemberEntity memberEntity = new MemberEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    null,
                    resultSet.getString("role")
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
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
          .withTableName("member")
          .usingGeneratedKeyColumns("id");

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", member.getName());
        columnMap.put("email", member.getEmail());
        columnMap.put("password", member.getPassword());
        columnMap.put("role", member.getRole().name());

        jdbcInsert.execute(columnMap);
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE email = ?";

        List<MemberEntity> memberEntities = jdbcTemplate.query(
            sql, new Object[] {email}, (resultSet, rowNum) -> {
                MemberEntity member = new MemberEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
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

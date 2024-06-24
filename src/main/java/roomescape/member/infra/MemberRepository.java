package roomescape.member.infra;

import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

import java.util.Map;

@Repository
public class MemberRepository {
    private static final Member member1 = new Member("제이슨", "json@email.com", "1234");
    private static final Member member2 = new Member("심슨", "simson@email.com", "1234");
    private static final Map<Long, Member> MemberStorage = Map.of(member1.getId(), member1, member2.getId(), member2);

    public boolean isExistByEmail(final Member member) {
        return MemberStorage.values().stream()
                .anyMatch(savedMember -> savedMember.getEmail().equals(member.getEmail()));
    }

    public boolean isExistMemBerByEmailAndPassword(final String email, final String password) {
        return MemberStorage.values().stream()
                .filter(savedMember -> savedMember.getEmail().equals(email))
                .anyMatch(savedMember -> savedMember.getPassword().equals(password));
    }

    public Long findIdByEmail(final String email) {
        return MemberStorage.values().stream()
                .filter(savedMember -> savedMember.getEmail().equals(email))
                .map(Member::getId).findFirst().orElse(null);
    }

    public String findNameById(final Long id) {
        return MemberStorage.values().stream()
                .filter(m -> m.getId().equals(id))
                .map(Member::getName).findFirst().orElse(null);
    }
}

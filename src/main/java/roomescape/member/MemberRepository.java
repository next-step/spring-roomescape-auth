package roomescape.member;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class MemberRepository {

    private static final Member member1 = new Member("제이슨", "json@email.com", "1234");
    private static final Member member2 = new Member("심슨", "simson@email.com", "1234");
    private static final Map<Long, Member> MemberStorage = Map.of(
            member1.getId(), member1,
            member2.getId(), member2);

    public boolean isExistByEmail(Member member){
        return MemberStorage.values().stream().anyMatch(
                m -> m.getEmail().equals(member.getEmail())
        );
    }

    public boolean isExistMemBerByEmailAndPassword(String email, String password) {
        return MemberStorage.values().stream()
                .filter(
                        m -> m.getEmail().equals(email)
                ).anyMatch(
                        m -> m.getPassword().equals(password)
                );
    }

    public Long findIdByEmail(String email) {
        return MemberStorage.values().stream()
                .filter(m -> m.getEmail().equals(email))
                .map(Member::getId).findFirst().orElse(null);
    }

    public String findNameById(Long id) {
        return MemberStorage.values().stream()
                .filter(m -> m.getId().equals(id))
                .map(Member::getName).findFirst().orElse(null);
    }
}

package roomescape.application.port.out;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberPort {

    List<Member> findMembers();

    void saveMember(Member member);

    Optional<Member> findMemberByEmail(String email);
}

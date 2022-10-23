package nextstep.domain.repository;

import java.util.Optional;
import nextstep.domain.Member;

public interface MemberRepository {

    Long save(Member member);

    Optional<Member> findBy(Long id);

    Optional<Member> findBy(String username);

    void deleteAll();
}

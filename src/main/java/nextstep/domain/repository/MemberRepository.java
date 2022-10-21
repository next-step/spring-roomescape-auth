package nextstep.domain.repository;

import nextstep.domain.Member;

public interface MemberRepository {

    Long save(Member member);

    Member findById(Long id);

    Member findByUsername(String username);
}

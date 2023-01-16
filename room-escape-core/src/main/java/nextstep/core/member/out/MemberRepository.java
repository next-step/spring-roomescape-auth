package nextstep.core.member.out;

import nextstep.core.member.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findByUsername(String username);

    Optional<Member> findById(Long memberId);
}

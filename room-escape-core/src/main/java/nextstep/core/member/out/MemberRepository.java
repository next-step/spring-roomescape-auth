package nextstep.core.member.out;

import nextstep.core.member.Member;

public interface MemberRepository {
    Member save(Member member);

    Member findById(Long id);

    Member findByUsername(String username);
}

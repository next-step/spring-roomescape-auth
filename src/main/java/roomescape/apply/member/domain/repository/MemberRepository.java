package roomescape.apply.member.domain.repository;

import roomescape.apply.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmailAndPassword(String email, String password);

    List<Member> findAll();

    Optional<Long> findIdByEmail(String email);

    Member save(Member member);

    Optional<Member> findOneByEmail(String email);

    Optional<Member> findOneById(long memberId);
}

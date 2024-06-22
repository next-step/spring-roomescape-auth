package roomescape.member.domain;

import roomescape.member.domain.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndPassword(String email, String password);
    long save(String name, String email, String password);
}

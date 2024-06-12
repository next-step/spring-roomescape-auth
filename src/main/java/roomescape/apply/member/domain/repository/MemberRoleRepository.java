package roomescape.apply.member.domain.repository;

import roomescape.apply.member.domain.MemberRole;

import java.util.Iterator;
import java.util.List;

public interface MemberRoleRepository {
    void saveAll(Iterator<MemberRole> memberRoles);

    List<String> findNamesByMemberId(Long memberId);
}

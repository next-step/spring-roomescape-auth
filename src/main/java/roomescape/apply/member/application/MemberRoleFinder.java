package roomescape.apply.member.application;

import org.springframework.stereotype.Service;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.domain.repository.MemberRoleRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemberRoleFinder {

    private final MemberRoleRepository memberRoleRepository;

    public MemberRoleFinder(MemberRoleRepository memberRoleRepository) {
        this.memberRoleRepository = memberRoleRepository;
    }

    public Set<MemberRoleName> findRolesInMember(long memberId) {
        return memberRoleRepository.findNamesByMemberId(memberId)
                .stream()
                .map(MemberRoleName::findRoleByValue)
                .collect(Collectors.toSet());
    }

}

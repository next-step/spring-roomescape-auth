package roomescape.apply.member.application;

import org.springframework.stereotype.Service;
import roomescape.apply.member.domain.MemberRole;
import roomescape.apply.member.domain.repository.MemberRoleRepository;

import java.util.Set;

@Service
public class MemberRoleSaver {

    private final MemberRoleRepository memberRoleRepository;

    public MemberRoleSaver(MemberRoleRepository memberRoleRepository) {
        this.memberRoleRepository = memberRoleRepository;
    }

    public void saveAll(Set<MemberRole> memberRoles) {
         memberRoleRepository.saveAll(memberRoles.iterator());
    }

}

package nextstep.application.service.member;

import nextstep.application.dto.member.MemberResponse;
import nextstep.domain.Member;
import nextstep.domain.service.MemberDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberQueryService {

    private final MemberDomainService memberDomainService;

    public MemberQueryService(MemberDomainService memberDomainService) {
        this.memberDomainService = memberDomainService;
    }

    public MemberResponse findBy(Long id) {
        return toResponse(memberDomainService.findBy(id));
    }

    public MemberResponse findBy(String username) {
        return toResponse(memberDomainService.findBy(username));
    }

    private MemberResponse toResponse(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getUsername(),
            member.getPassword(),
            member.getName(),
            member.getPhone(),
            member.getRole()
        );
    }
}

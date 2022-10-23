package nextstep.application.service.member;

import nextstep.application.dto.member.MemberRequest;
import nextstep.domain.Member;
import nextstep.domain.service.MemberDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberCommandService {

    private final MemberDomainService memberDomainService;

    public MemberCommandService(MemberDomainService memberDomainService) {
        this.memberDomainService = memberDomainService;
    }

    public Long create(MemberRequest request) {
        return memberDomainService.save(member(request));
    }

    private Member member(MemberRequest request) {
        return new Member(
            request.getUsername(),
            request.getPassword(),
            request.getName(),
            request.getPhone()
        );
    }

    public void deleteAll() {
        memberDomainService.deleteAll();
    }
}

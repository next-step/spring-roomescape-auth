package roomescape.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.adapter.mapper.MemberMapper;
import roomescape.application.dto.MemberCommand;
import roomescape.application.dto.MemberResponse;
import roomescape.application.port.in.MemberUseCase;
import roomescape.application.port.out.MemberPort;

@Service
public class MemberService implements MemberUseCase {

    private final MemberPort memberPort;

    public MemberService(MemberPort memberPort) {
        this.memberPort = memberPort;
    }

    @Override
    public List<MemberResponse> findMembers() {
        return memberPort.findMembers()
                         .stream()
                         .map(MemberMapper::mapToResponse)
                         .toList();
    }

    @Override
    public void registerMember(MemberCommand memberCommand) {
        memberPort.saveMember(MemberMapper.mapToDomain(memberCommand));
    }
}

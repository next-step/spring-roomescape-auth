package nextstep.member;

import nextstep.member.presentation.dto.CreateMemberRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(CreateMemberRequest createMemberRequest) {
        return memberDao.save(createMemberRequest.toEntity(UUID.randomUUID().toString()));
    }

    public Member findByUuid(String uuid) {
        return memberDao.findByUuid(uuid).orElseThrow();
    }
}

package nextstep.member;

import nextstep.member.presentation.dto.CreateMemberRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(CreateMemberRequest createMemberRequest) {
        return memberDao.save(createMemberRequest.toEntity(UUID.randomUUID().toString(), List.of("USER")));
    }

    public Long createAdmin(CreateMemberRequest createMemberRequest) {
        return memberDao.save(createMemberRequest.toEntity(UUID.randomUUID().toString(), List.of("ADMIN")));
    }

    public Member findByUuid(String uuid) {
        return memberDao.findByUuid(uuid).orElseThrow();
    }
}

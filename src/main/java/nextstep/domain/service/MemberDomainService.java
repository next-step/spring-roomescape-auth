package nextstep.domain.service;

import nextstep.common.exception.MemberException;
import nextstep.domain.Member;
import nextstep.infrastructure.MemberDao;
import org.springframework.stereotype.Service;

@Service
public class MemberDomainService {

    private final MemberDao memberDao;

    public MemberDomainService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long save(Member member) {
        return memberDao.save(member);
    }

    public Member findBy(Long id) {
        return memberDao.findBy(id)
            .orElseThrow(() -> new MemberException("존재하지 않는 사용자입니다."));
    }

    public Member findBy(String username) {
        return memberDao.findBy(username)
            .orElseThrow(() -> new MemberException("존재하지 않는 사용자입니다."));
    }

    public void deleteAll() {
        memberDao.deleteAll();
    }
}

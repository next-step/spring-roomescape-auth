package nextstep.member;

import nextstep.support.DuplicateEntityException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        if (memberDao.existsByUsername(memberRequest.getUsername())) {
            throw new DuplicateEntityException("입력하신 username은 이미 사용중입니다.");
        }
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }
}

package nextstep.member;

import nextstep.auth.LoginInfo;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findByLoginInfo(LoginInfo loginInfo) {
        return memberDao.findByUsername(loginInfo.getUsername());
    }
}

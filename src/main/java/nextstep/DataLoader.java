package nextstep;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.member.MemberRole;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final MemberDao memberDao;

    public DataLoader(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Member member = new Member("super_admin", "어드민국룰비밀번호q1w2e3r4", "찰리", "01071678343", MemberRole.ADMIN);
        memberDao.save(member);
    }
}

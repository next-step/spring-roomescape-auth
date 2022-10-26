package nextstep.support;

import nextstep.member.MemberDao;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static nextstep.member.Member.ADMIN;

@Component
public class AdminDataLoader implements ApplicationRunner {

    private final MemberDao memberDao;

    public AdminDataLoader(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void run(ApplicationArguments args) {
        memberDao.save(ADMIN);
    }
}
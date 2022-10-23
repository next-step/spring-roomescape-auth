package nextstep.dataloader;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminMemberLoader implements CommandLineRunner {

    public static final String ADMIN_USERNAME = "adminname";
    public static final String ADMIN_PASSWORD = "password";

    private MemberDao memberDao;

    public AdminMemberLoader(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void run(String... args) {
        memberDao.save(
            Member.createAdmin(ADMIN_USERNAME, ADMIN_PASSWORD, "admin", "010-1111-2222")
        );
    }
}

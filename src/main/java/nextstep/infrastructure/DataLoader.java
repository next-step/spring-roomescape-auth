package nextstep.infrastructure;

import nextstep.common.Role;
import nextstep.domain.Member;
import nextstep.infrastructure.member.MemberDao;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

// ref. https://sgc109.github.io/2020/07/09/spring-running-startup-logic/#ApplicationListener
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final MemberDao memberDao;

    public DataLoader(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Member admin = new Member("admin", "admin", "단이", "010-1234-5678", Role.ADMIN);
        memberDao.save(admin);
    }
}

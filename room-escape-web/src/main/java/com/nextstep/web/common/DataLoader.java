package com.nextstep.web.common;

import com.nextstep.web.member.repository.MemberDao;
import com.nextstep.web.member.repository.entity.MemberEntity;
import nextstep.domain.member.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataLoader  {
    private final MemberDao memberDao;

    public DataLoader(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        MemberEntity admin =
                new MemberEntity(null,"master", "1234", "master", "0000-000-0000", Role.ADMIN.name());
        memberDao.save(admin);

    }
}

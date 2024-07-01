package roomescape.member.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRole;
import roomescape.member.service.MemberRepository;

@Component
public class MemberInitializer implements ApplicationRunner {

    public static final String DUMMY_USER_EMAIL = "test@test.com";
    public static final String DUMMY_USER_PASSWORD = "password";
    public static final String DUMMY_USER_NAME = "test";
    public static final String DUMMY_ADMIN_EMAIL = "admin@test.com";
    public static final String DUMMY_ADMIN_PASSWORD = "password";
    public static final String DUMMY_ADMIN_NAME = "admin";

    private final MemberRepository memberRepository;

    public MemberInitializer(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberRepository.save(new Member(DUMMY_USER_EMAIL, DUMMY_USER_PASSWORD, DUMMY_USER_NAME, MemberRole.MEMBER));
        memberRepository.save(new Member(DUMMY_ADMIN_EMAIL, DUMMY_ADMIN_PASSWORD, DUMMY_ADMIN_NAME, MemberRole.ADMIN));
    }
}

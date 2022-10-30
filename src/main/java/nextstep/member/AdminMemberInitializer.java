package nextstep.member;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminMemberInitializer implements ApplicationRunner {
    private final MemberService memberService;

    public AdminMemberInitializer(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void run(ApplicationArguments args) {
        memberService.create(new MemberRequest("kouz", "it's my secret", "김경준", "01012341234", "ADMIN"));
    }
}

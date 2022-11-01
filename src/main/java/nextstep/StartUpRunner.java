package nextstep;

import nextstep.member.MemberRequest;
import nextstep.member.MemberService;
import nextstep.member.Role;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartUpRunner implements ApplicationRunner {

    private final MemberService memberService;

    public StartUpRunner(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void run(ApplicationArguments args) {
        memberService.create(
            new MemberRequest(
                "username",
                "password",
                "name",
                "010-1234-1234",
                Role.ADMIN
            )
        );
    }
}

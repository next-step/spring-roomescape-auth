package nextstep;

import nextstep.member.MemberService;
import nextstep.member.presentation.dto.CreateMemberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "password";

    @Autowired
    private MemberService memberService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CreateMemberRequest memberRequest = new CreateMemberRequest(ADMIN_USERNAME, ADMIN_PASSWORD, "admin", "010-1234-5678");
        memberService.createAdmin(memberRequest);
    }
}

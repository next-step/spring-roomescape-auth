package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.member.MemberRequest;
import nextstep.member.MemberService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseDataLoader implements ApplicationRunner {

  private final MemberService memberService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    // example member 생성
    memberService.create(
        MemberRequest.builder()
            .username("manager")
            .name("manager")
            .password("password")
            .phone("010-1234-5678")
            .build());
  }
}

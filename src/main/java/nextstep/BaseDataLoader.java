package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.member.MemberRequest;
import nextstep.member.MemberService;
import nextstep.member.RoleType;
import nextstep.schedule.ScheduleRequest;
import nextstep.schedule.ScheduleService;
import nextstep.theme.ThemeRequest;
import nextstep.theme.ThemeService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseDataLoader implements ApplicationRunner {

  private final MemberService memberService;
  private final ThemeService themeService;
  private final ScheduleService scheduleService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    // example admin member 생성
    memberService.create(
        MemberRequest.builder()
            .username("manager")
            .name("manager")
            .password("password")
            .phone("010-1234-5678")
            .role(RoleType.ADMIN)
            .build());

    memberService.create(
        MemberRequest.builder()
            .username("user")
            .name("user")
            .password("password")
            .phone("010-1234-5678")
            .role(RoleType.USER)
            .build());

    var themeId = themeService.create(
        ThemeRequest.builder()
            .name("재미난테마")
            .desc("재미난 것을 함")
            .price(100_000_000)
            .build()
    );

    scheduleService.create(
        ScheduleRequest.builder()
            .themeId(themeId)
            .date("2022-11-11")
            .time("13:00")
            .build()
    );
  }
}

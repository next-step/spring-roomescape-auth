package nextstep.app.web;

import nextstep.app.web.auth.JwtTokenProvider;
import nextstep.app.web.member.adapter.in.MemberService;
import nextstep.app.web.reservation.adapter.in.ReservationService;
import nextstep.app.web.schedule.adapter.in.ScheduleService;
import nextstep.app.web.theme.adapter.in.ThemeService;
import nextstep.core.member.MemberRole;
import nextstep.core.member.in.MemberRegisterRequest;
import nextstep.core.member.in.MemberResponse;
import nextstep.core.reservation.in.ReservationCreateRequest;
import nextstep.core.schedule.in.ScheduleCreateRequest;
import nextstep.core.schedule.in.ScheduleResponse;
import nextstep.core.theme.in.ThemeCreateRequest;
import nextstep.core.theme.in.ThemeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Profile("local")
@Component
public class RoomEscapeApplicationRunner implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomEscapeApplicationRunner.class);

    private final ThemeService themeService;
    private final ScheduleService scheduleService;
    private final MemberService memberService;
    private final ReservationService reservationService;
    private final JwtTokenProvider jwtTokenProvider;

    public RoomEscapeApplicationRunner(ThemeService themeService, ScheduleService scheduleService, MemberService memberService, ReservationService reservationService, JwtTokenProvider jwtTokenProvider) {
        this.themeService = themeService;
        this.scheduleService = scheduleService;
        this.memberService = memberService;
        this.reservationService = reservationService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void run(ApplicationArguments args) {
        ThemeResponse themeResponse = themeService.create(new ThemeCreateRequest("name", "desc", 20000L));

        ScheduleResponse scheduleResponse = scheduleService.create(new ScheduleCreateRequest(themeResponse.getId(), LocalDate.parse("2022-08-11"), LocalTime.parse("13:00")));

        MemberResponse user = memberService.register(new MemberRegisterRequest("user", "user", "user", MemberRole.USER, "010-0000-0000"));
        MemberResponse admin = memberService.register(new MemberRegisterRequest("admin", "admin", "admin", MemberRole.ADMIN, "010-0000-0000"));

        reservationService.create(new ReservationCreateRequest(scheduleResponse.getId()), user.getId());

        LOGGER.info("user token: " + jwtTokenProvider.createToken(user.getId().toString(), List.of("USER")));
        LOGGER.info("admin token: " + jwtTokenProvider.createToken(admin.getId().toString(), List.of("ADMIN")));
    }
}

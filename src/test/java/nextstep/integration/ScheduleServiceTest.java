package nextstep.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.application.dto.schedule.ScheduleRequest;
import nextstep.application.dto.schedule.ScheduleResponse;
import nextstep.application.dto.theme.ThemeRequest;
import nextstep.application.dto.theme.ThemeResponse;
import nextstep.application.service.reservation.ReservationCommandService;
import nextstep.application.service.schedule.ScheduleCommandService;
import nextstep.application.service.schedule.ScheduleQueryService;
import nextstep.application.service.theme.ThemeCommandService;
import nextstep.common.exception.ScheduleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScheduleServiceTest {

    @Autowired
    private ReservationCommandService reservationCommandService;

    @Autowired
    private ScheduleCommandService scheduleCommandService;

    @Autowired
    private ScheduleQueryService scheduleQueryService;

    @Autowired
    private ThemeCommandService themeCommandService;

    @AfterEach
    void tearDown() {
        reservationCommandService.cancelAll();
        scheduleCommandService.cancelAll();
        themeCommandService.deleteAll();
    }

    @DisplayName("테마별 스케줄을 생성한다.")
    @Test
    void make() {
        // given
        ThemeRequest themeRequest = new ThemeRequest("열쇠공이", "열쇠공이의 이중생활", 25000);
        Long themeId = themeCommandService.create(themeRequest);

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, "2022-08-11", "16:00");

        // when
        Long scheduleId = scheduleCommandService.make(scheduleRequest);

        // then
        assertThat(scheduleId).isNotNull();
    }

    @DisplayName("테마별 스케줄 목록을 조회한다.")
    @Test
    void checkAll() {
        // given
        ThemeRequest themeRequest = new ThemeRequest("열쇠공이", "열쇠공이의 이중생활", 25000);
        Long themeId = themeCommandService.create(themeRequest);

        ScheduleRequest scheduleRequest1 = new ScheduleRequest(themeId, "2022-08-11", "16:00");
        ScheduleRequest scheduleRequest2 = new ScheduleRequest(themeId, "2022-08-11", "20:00");
        scheduleCommandService.make(scheduleRequest1);
        scheduleCommandService.make(scheduleRequest2);

        ThemeResponse themeResponse = new ThemeResponse(null, "열쇠공이", "열쇠공이의 이중생활", 25000);
        List<ScheduleResponse> expected = List.of(
            new ScheduleResponse(null, themeResponse, "2022-08-11", "16:00"),
            new ScheduleResponse(null, themeResponse, "2022-08-11", "20:00")
        );

        // when
        List<ScheduleResponse> responses = scheduleQueryService.checkAll(themeId, "2022-08-11");

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses)
            .usingRecursiveComparison()
            .ignoringFields("id", "theme.id")
            .isEqualTo(expected);
    }

    @DisplayName("스케줄을 삭제한다.")
    @Test
    void cancel_success() {
        // given
        ThemeRequest themeRequest = new ThemeRequest("열쇠공이", "열쇠공이의 이중생활", 25000);
        Long themeId = themeCommandService.create(themeRequest);

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, "2022-08-11", "16:00");
        Long scheduleId = scheduleCommandService.make(scheduleRequest);

        reservationCommandService.cancelAll();

        // when
        // then
        assertThatCode(() -> scheduleCommandService.cancel(scheduleId)).doesNotThrowAnyException();
    }

    @DisplayName("예약과 관련 있는 스케줄은 삭제할 수 없다.")
    @Test
    void cancel_fail1() {
        // given
        ThemeRequest themeRequest = new ThemeRequest("열쇠공이", "열쇠공이의 이중생활", 25000);
        Long themeId = themeCommandService.create(themeRequest);

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, "2022-08-11", "16:00");
        Long scheduleId = scheduleCommandService.make(scheduleRequest);

        // when
        // then
        assertThatThrownBy(() -> scheduleCommandService.cancel(scheduleId))
            .isInstanceOf(ScheduleException.class)
            .hasMessage("스케줄을 삭제할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 스케줄은 삭제할 수 없다.")
    @Test
    void cancel_fail2() {
        // given
        // when
        // then
        assertThatThrownBy(() -> scheduleCommandService.cancel(Long.MAX_VALUE))
            .isInstanceOf(ScheduleException.class)
            .hasMessage("존재하지 않는 스케줄입니다.");
    }
}

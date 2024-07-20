package roomescape.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.reservation.create.ReservationCreateRequest;
import roomescape.dto.theme.create.ThemeCreateRequest;
import roomescape.dto.theme.create.ThemeCreateResponse;
import roomescape.dto.time.ReservationTimeRequest;
import roomescape.dto.time.ReservationTimeResponse;
import roomescape.dto.time.available.ReservationTimeAvailableResponse;
import roomescape.dto.time.create.ReservationTimeCreateResponse;
import roomescape.exception.custom.DuplicatedReservationTimeException;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class ReservationTimeServiceTest {

    @Autowired
    ReservationTimeService reservationTimeService;

    @Autowired
    ThemeService themeService;

    @Autowired
    ReservationService reservationService;

    @Test
    @DisplayName("예약 시간 리스트 테스트")
    void findTimes() {
        List<String> collect = reservationTimeService.findTimes().stream()
                .map(t -> t.getStartAt().toString())
                .collect(Collectors.toList());

        //더미 데이터 3개
        assertThat(collect.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("예약 시간 추가")
    void createTime() {
        //given
        ReservationTimeRequest createTime = new ReservationTimeRequest("16:00");
        ReservationTimeRequest createTime2 = new ReservationTimeRequest("15:00");

        //기존의 더미 데이터 3개
        //현재 두개의 예약시간만 등록한다는 가정
        ReservationTimeCreateResponse time = reservationTimeService.createTime(createTime);
        ReservationTimeCreateResponse time2 = reservationTimeService.createTime(createTime2);

        //then
        List<ReservationTimeResponse> list = reservationTimeService.findTimes();

        assertThat(list.size()).isEqualTo(5);
        assertThat(list.get(3).getStartAt()).isEqualTo(createTime.getStartAt());
    }

    @Test
    @DisplayName("예약 시간 삭제")
    void deleteTime() {

        reservationTimeService.deleteTime(1L);
        List<ReservationTimeResponse> times = reservationTimeService.findTimes();
        assertThat(times.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("예외 발생")
    void checkException() {
        //init 메서드에 12:00 존재.
        //DuplicatedReservationTimeException 이미 등록된 예약시간에서 예외 발생
        assertThrows(DuplicatedReservationTimeException.class, ()->
            reservationTimeService.createTime(new ReservationTimeRequest("12:00")));
    }

    @Test
    @DisplayName("예약 가능한 시간대 조회 테스트")
    void findAvailableReservationTime() {

        //given
        ThemeCreateResponse theme = themeService.createTheme(new ThemeCreateRequest("무서운 이야기",
                "너무 무서움ㄷㄷ너무 무서움ㄷㄷ너무 무서움ㄷㄷ너무 무서움ㄷㄷ너무 무서움ㄷㄷ", "GOOD"));
        String date = "2024-09-24";

        //when
        List<ReservationTimeAvailableResponse> response =
                reservationTimeService.findAvailableReservationTime(date, theme.getId());

        //then
        assertThat(response.size()).isEqualTo(3);
        assertThat(response.get(0).getStartAt()).isEqualTo("12:00");
    }

    @Test
    @DisplayName("예약 가능한 시간대 조회 실패 테스트")
    void failAvailableReservationTime() {
        //given
        String date = "2024-09-24";
        Long themeId = 1L;
        reservationService.createReservation(new ReservationCreateRequest(date, "brown", 1L, 1L));
        reservationService.createReservation(new ReservationCreateRequest(date, "brown", 2L, 1L));
        reservationService.createReservation(new ReservationCreateRequest(date, "brown", 3L, 1L));

        //이미 예약이 차서 가능한 시간대 미존재
        //when
        List<ReservationTimeAvailableResponse> response =
                reservationTimeService.findAvailableReservationTime(date, themeId);

        //then
        assertThat(response.size()).isEqualTo(0);
    }
}
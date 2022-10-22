package nextstep.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.application.service.reservation.ReservationCommandService;
import nextstep.application.service.reservation.ReservationQueryService;
import nextstep.application.dto.reservation.ReservationRequest;
import nextstep.application.dto.reservation.ReservationResponse;
import nextstep.common.exception.ReservationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationCommandService reservationCommandService;

    @Autowired
    private ReservationQueryService reservationQueryService;


    @BeforeEach
    void setUp() {
        ReservationRequest request = new ReservationRequest("2022-08-11", "13:00", "dani");
        reservationCommandService.make(request);
    }

    @AfterEach
    void tearDown() {
        reservationCommandService.cancelAll();
    }

    @DisplayName("예약을 생성한다.")
    @Test
    void make_success() {
        // given
        ReservationRequest request = new ReservationRequest("2022-08-11", "14:00", "dani");

        // when
        Long reservationId = reservationCommandService.make(request);

        // then
        assertThat(reservationId).isNotNull();
    }

    @DisplayName("예약을 생성할 때, `날짜`와 `시간`이 똑같은 예약이 있으면 예약을 생성할 수 없다.")
    @Test
    void make_fail() {
        // given
        ReservationRequest request = new ReservationRequest("2022-08-11", "13:00", "daisy");

        // when
        // then
        assertThatThrownBy(() -> reservationCommandService.make(request))
            .isInstanceOf(ReservationException.class)
            .hasMessageContaining("이미 예약이 차있습니다.");
    }

    @DisplayName("예약 유무를 확인한다. - 있음")
    @Test
    void exist_true() {
        // given
        Long reservationId = reservationQueryService.checkAll("2022-08-11").stream()
            .filter(reservation -> "13:00".equals(reservation.getTime()))
            .findFirst()
            .map(ReservationResponse::getId)
            .orElseThrow();

        // when
        // then
        assertThat(reservationQueryService.exist(reservationId)).isTrue();
    }

    @DisplayName("예약 유무를 확인한다. - 없음")
    @Test
    void exist_false() {
        // given
        // when
        // then
        assertThat(reservationQueryService.exist(Long.MAX_VALUE)).isFalse();
    }

    @DisplayName("예약 목록을 조회한다.")
    @Test
    void checkAll() {
        // given
        ReservationRequest request = new ReservationRequest("2022-08-11", "14:00", "dani");
        reservationCommandService.make(request);

        List<ReservationResponse> expected = List.of(
            new ReservationResponse(null, "2022-08-11", "13:00", "dani"),
            new ReservationResponse(null, "2022-08-11", "14:00", "dani")
        );

        // when
        List<ReservationResponse> responses = reservationQueryService.checkAll("2022-08-11");

        // then
        assertThat(responses)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expected);
    }

    @DisplayName("예약 목록을 조회할 때, 예약이 없으면 빈 목록을 조회한다.")
    @Test
    void checkAll_empty() {
        // given
        reservationCommandService.cancel("2022-08-11", "13:00");

        // when
        List<ReservationResponse> responses = reservationQueryService.checkAll("2022-08-11");

        // then
        assertThat(responses).isEmpty();
    }

    @DisplayName("예약을 취소한다.")
    @Test
    void cancel_success() {
        // given
        // when
        // then
        assertThatCode(() -> reservationCommandService.cancel("2022-08-11", "13:00"))
            .doesNotThrowAnyException();
    }

    @DisplayName("예약을 취소할 때, 해당 `날짜`와 `시간`에 아무 예약도 존재하지 않으면 예약을 취소할 수 없다.")
    @Test
    void cancel_fail() {
        // given
        // when
        // then
        assertThatThrownBy(() -> reservationCommandService.cancel("2022-08-12", "13:00"))
            .isInstanceOf(ReservationException.class)
            .hasMessage("존재하는 예약이 없어 예약을 취소할 수 없습니다.");
    }
}

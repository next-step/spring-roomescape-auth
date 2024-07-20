package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.reservation.ReservationsResponse;
import roomescape.dto.reservation.create.ReservationCreateRequest;
import roomescape.dto.reservation.create.ReservationCreateResponse;
import roomescape.dto.theme.create.ThemeCreateRequest;
import roomescape.dto.time.ReservationTimeRequest;
import roomescape.exception.custom.InvalidReservationTimeException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationTimeService reservationTimeService;
    @Autowired
    ThemeService themeService;

    @BeforeEach
    void init() {
        reservationService.createReservation(new ReservationCreateRequest("2024-08-22", "hello", 1L, 1L));
    }

    @Test
    @DisplayName("예약된 정보 리스트 확인")
    void findReservations() {
        List<ReservationsResponse> reservations = reservationService.findReservations();

        assertThat(reservations.size()).isEqualTo(1);
        assertThat(reservations.get(0).getName()).isEqualTo("hello");
    }

    @Test
    @DisplayName("예약 등록")
    void createReservation() {
        //init의 등록한 reservation은 주석 처리했다는 가정
        //given
        ReservationCreateResponse reservation = reservationService.createReservation
                (new ReservationCreateRequest("2024-07-22", "hello", 1L, 1L));

        //when
        List<ReservationsResponse> list = reservationService.findReservations();

        //then
        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(1).getDate()).isEqualTo("2024-07-22");
    }

    @Test
    @DisplayName("해당 예약을 삭제한다.")
    void deleteReservation() {
        //given
        List<ReservationsResponse> reservations = reservationService.findReservations();
        //when
        reservationService.deleteReservation(reservations.get(0).getId());
        //then
        List<ReservationsResponse> list = reservationService.findReservations();
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("예약시간이 현재 시간보다 이전일 경우 예외를 발생시킨다.")
    void checkException(){
        //과거의 시간으로 등록을 했을 때 throw new ErrorCodeResponse(ErrorCode.INVALID_TIME_ZONE, "예약 시간을 다시 확인해주세요.");
        //예외 발생
        org.junit.jupiter.api.Assertions.assertThrows(InvalidReservationTimeException.class, () -> {
            reservationService.createReservation(new ReservationCreateRequest("2024-05-20", "hello", 1L, 1L));
        });
    }
}
package roomescape.time.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.repository.ReservationTimeRepository;
import roomescape.time.dto.ReservationTimeResponse;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약이 가능한 시간을 조회한다.")
    void getAvailableReservationTimes() {
        // given
        Theme theme = new Theme(1L, "레벨1 탈출", "우테코 레벨1을 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        List<ReservationTime> times = List.of(new ReservationTime(1L, "10:00"), new ReservationTime(2L, "12:00"));

        given(themeRepository.findById(anyLong())).willReturn(Optional.of(theme));
        given(reservationTimeRepository.findAll()).willReturn(times);

        LocalDate date = LocalDate.parse("2021-07-01");

        // when
        List<ReservationTimeResponse> availableTimes = reservationTimeService.getAvailableReservationTimes(date, theme.getId());

        // then
        assertThat(availableTimes).hasSize(2);
    }

    @Test
    @DisplayName("테마를 찾을 수 없는 경우 예외가 발생한다.")
    void getAvailableReservationTimes_Fail_WhenThemeNotFound() {
        given(themeRepository.findById(anyLong())).willThrow(new ThemeNotFoundException());

        assertThatThrownBy(() -> reservationTimeService.getAvailableReservationTimes(LocalDate.parse("2021-07-01"), 1L))
                .isInstanceOf(ThemeNotFoundException.class)
                .hasMessage("해당 테마가 존재하지 않습니다.");
    }
}

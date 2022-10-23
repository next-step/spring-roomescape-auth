package nextstep.reservation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import nextstep.auth.AuthenticationException;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReservationTest {

    @DisplayName("사용자의 예약이 아니면 예외가 발생한다.")
    @Test
    void validateOwner() {
        Schedule schedule = new Schedule(
            new Theme("갈색테마", "브라운", 30_000),
            LocalDate.of(2022, 10, 23),
            LocalTime.of(10, 0)
        );

        Reservation reservation = new Reservation(schedule, 1L, "최현구");

        assertThatThrownBy(() -> reservation.validateOwner(2L))
            .isExactlyInstanceOf(AuthenticationException.class);
    }
}

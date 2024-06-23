package roomescape.reservation.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public record ReservationCreateRequest(@NotNull(message = "날짜는 필수 입력 값입니다.")
                                       String date,
                                       @NotNull(message = "테마 ID는 필수 입력 값입니다.")
                                       Long themeId,
                                       @NotNull(message = "시간 ID는 필수 입력 값입니다.")
                                       Long timeId,
                                       @NotNull(message = "회원 ID는 필수 입력 값입니다.")
                                       Long memberId) {

    public Reservation toReservation(String name, ReservationTime savedReservationTime, Theme savedTheme) {
        return new Reservation(name, LocalDate.parse(date), savedReservationTime, savedTheme);
    }
}
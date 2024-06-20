package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;

public record UserReservationCreateRequest(@NotBlank(message = "예약 날짜는 필수 입력 값입니다.")
                                           String date,
                                           Long timeId,
                                           Long themeId) {

    public ReservationCreateRequest toReservationCreateRequest(final Long id) {
        return new ReservationCreateRequest(date, themeId, timeId, id);
    }
}

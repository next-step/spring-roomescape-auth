package roomescape.GloblaFixture.dto;

import roomescape.reservationTheme.dto.ReservationThemeRequestDto;

public class ReservationThemeDtoFixture {

    public static ReservationThemeRequestDto createReservationThemeDto() {
        return new ReservationThemeRequestDto(1L, "테마1", "설명1", "썸네일1");
    }
}

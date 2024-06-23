package roomescape.GloblaFixture.dto;

import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservationTheme.dto.ReservationThemeRequestDto;
import roomescape.reservationTime.dto.ReservationTimeRequestDto;

public class ReservationDtoFixture {

    public static ReservationRequestDto createReservationDto(){
        final ReservationThemeRequestDto reservationThemeDto = ReservationThemeDtoFixture.createReservationThemeDto();
        final ReservationTimeRequestDto reservationTimeRequestDto = ReservationTimeDtoFixture.createReservationTimeRequestDto();
        return new ReservationRequestDto("김준성", "2025-08-15", reservationTimeRequestDto, reservationThemeDto);
    }
}

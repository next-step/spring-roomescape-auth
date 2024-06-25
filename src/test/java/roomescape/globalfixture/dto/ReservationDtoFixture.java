package roomescape.globalfixture.dto;

import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservationtheme.dto.ReservationThemeRequestDto;
import roomescape.reservationtime.dto.ReservationTimeRequestDto;

public class ReservationDtoFixture {

    public static ReservationRequestDto createReservationDto(){
        final ReservationThemeRequestDto reservationThemeDto = ReservationThemeDtoFixture.createReservationThemeDto();
        final ReservationTimeRequestDto reservationTimeRequestDto = ReservationTimeDtoFixture.createReservationTimeRequestDto();
        return new ReservationRequestDto("김준성", "2025-08-15", reservationTimeRequestDto, reservationThemeDto);
    }
}

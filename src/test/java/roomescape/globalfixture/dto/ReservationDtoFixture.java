package roomescape.globalfixture.dto;

import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.dto.TimeDto;
import roomescape.reservationtheme.dto.ReservationThemeRequestDto;
import roomescape.reservationtime.dto.ReservationTimeRequestDto;

public class ReservationDtoFixture {

    public static ReservationRequestDto createReservationDto(){
        final ReservationThemeRequestDto reservationThemeDto = ReservationThemeDtoFixture.createReservationThemeDto();
        final TimeDto timeDto = TimeDtoFixture.timeDtoCreate();
        return new ReservationRequestDto(null, "2025-08-15", timeDto, reservationThemeDto);
    }
}

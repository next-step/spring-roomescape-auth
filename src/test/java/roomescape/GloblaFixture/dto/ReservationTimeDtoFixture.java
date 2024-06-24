package roomescape.GloblaFixture.dto;

import roomescape.reservationTime.dto.ReservationTimeRequestDto;

public class ReservationTimeDtoFixture {

    public static ReservationTimeRequestDto createReservationTimeRequestDto(){
        return new ReservationTimeRequestDto(1L, "10:00");
    }
}

package roomescape.GloblaFixture.entity;

import roomescape.reservationTheme.domain.ReservationTheme;

public class ReservationThemeFixture {

    public static ReservationTheme createReservationTheme(){
        return new ReservationTheme(1L,"테마1", "설명1", "썸네일1");
    }
}

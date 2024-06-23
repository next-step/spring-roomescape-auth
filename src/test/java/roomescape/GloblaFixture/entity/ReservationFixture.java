package roomescape.GloblaFixture.entity;

import roomescape.reservation.domain.Reservation;
import roomescape.reservationTheme.domain.ReservationTheme;
import roomescape.reservationTime.domain.ReservationTime;

public class ReservationFixture {

    public static Reservation createReservation(){
        final ReservationTime reservationTime = ReservationTimeFixture.createReservationTime();
        final ReservationTheme reservationTheme = ReservationThemeFixture.createReservationTheme();
        return new Reservation("김준성", "2025-08-15", reservationTime, reservationTheme);
    }

}

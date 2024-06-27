package roomescape.globalfixture.entity;

import roomescape.reservation.domain.Reservation;
import roomescape.reservationtheme.domain.ReservationTheme;
import roomescape.reservationtime.domain.ReservationTime;

public class ReservationFixture {

    public static Reservation createReservation(){
        final ReservationTime reservationTime = ReservationTimeFixture.createReservationTime();
        final ReservationTheme reservationTheme = ReservationThemeFixture.createReservationTheme();
        return new Reservation(null,"김준성", "2025-08-15", reservationTime, reservationTheme);
    }

}

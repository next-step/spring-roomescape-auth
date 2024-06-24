package roomescape.GloblaFixture.entity;

import roomescape.reservationTime.domain.ReservationTime;

public class ReservationTimeFixture {

    public static ReservationTime createReservationTime(){
        return new ReservationTime(1L,"10:00");
    }
}

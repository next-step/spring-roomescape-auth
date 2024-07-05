package roomescape.domain.reservation;

import roomescape.domain.reservationtime.vo.ReservationTimeId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Reservations {

    private final List<Reservation> reservations;

    public Reservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Reservation> getReservations() {
        return List.copyOf(this.reservations);
    }

    public Reservations add(Reservation reservation) {
        ArrayList<Reservation> reservations = new ArrayList<>(this.reservations);
        reservations.add(reservation);

        return new Reservations(List.copyOf(reservations));
    }

    public List<ReservationTimeId> fetchTimeIds() {
        return this.reservations.stream()
                .map(Reservation::getTimeId)
                .collect(Collectors.toList());
    }
}

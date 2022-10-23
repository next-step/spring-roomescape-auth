package nextstep.domain.reservation;

import nextstep.domain.Identity;
import nextstep.domain.schedule.Schedule;

import java.time.LocalDateTime;

public class Reservation {
    private final Identity id;
    private final Schedule schedule;
    private final LocalDateTime reservationTime;
    private final String name;

    public Reservation(Identity id, Schedule schedule, LocalDateTime reservationTime, String name) {
        this.id = id;
        this.schedule = schedule;
        this.reservationTime = reservationTime;
        this.name = name;
    }

    public Identity getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public String getName() {
        return name;
    }

    public boolean isReservationBy(String name) {
       return this.name.equals(name);
    }
}

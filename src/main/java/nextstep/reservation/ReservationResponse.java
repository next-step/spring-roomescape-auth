package nextstep.reservation;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationResponse {

    private final Long id;
    private final String themeName;
    private final LocalDate date;
    private final LocalTime time;
    private final Long memberId;
    private final String name;

    public ReservationResponse(
        Long id,
        String themeName,
        LocalDate date,
        LocalTime time,
        Long memberId,
        String name
    ) {
        this.id = id;
        this.themeName = themeName;
        this.date = date;
        this.time = time;
        this.memberId = memberId;
        this.name = name;
    }

    public static List<ReservationResponse> of(List<Reservation> reservations) {
        return reservations.stream()
            .map(ReservationResponse::from)
            .collect(toList());
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getThemeName(),
            reservation.getScheduleDate(),
            reservation.getScheduleTime(),
            reservation.getMemberId(),
            reservation.getName()
        );
    }

    public Long getId() {
        return id;
    }

    public String getThemeName() {
        return themeName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }
}

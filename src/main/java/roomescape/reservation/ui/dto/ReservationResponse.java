package roomescape.reservation.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.theme.ui.dto.ThemeResponse;
import roomescape.reservation.domain.entity.Reservation;

import java.util.List;

public class ReservationResponse {
    private final Long id;
    @JsonProperty(value = "name")
    private final String memberName;
    private final String date;
    private final ReservationTimeResponse time;
    private final ThemeResponse theme;

    private ReservationResponse(Long id, String name, String date, ReservationTimeResponse time, ThemeResponse theme) {
        this.id = id;
        this.memberName = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

    public static List<ReservationResponse> fromReservations(List<Reservation> reservations) {
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    public Long getId() {
        return id;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeResponse getTime() {
        return time;
    }

    public ThemeResponse getTheme() {
        return theme;
    }
}

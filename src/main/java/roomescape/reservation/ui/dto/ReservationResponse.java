package roomescape.reservation.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.theme.ui.dto.ThemeResponse;
import roomescape.reservation.domain.entity.Reservation;

import java.util.List;

public record ReservationResponse(
    Long id,
    @JsonProperty(value = "name")
    String memberName,
    String date,
    ReservationTimeResponse time,
    ThemeResponse theme
){
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
}

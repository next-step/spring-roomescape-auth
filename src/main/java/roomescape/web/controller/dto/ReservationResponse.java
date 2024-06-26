package roomescape.web.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationResponse(Long id, String name, String date, ReservationTimeResponse time,
		ThemeResponse theme) {

	public static ReservationResponse from(Reservation reservation, ReservationTime reservationTime, Theme theme) {
		return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
				ReservationTimeResponse.from(reservationTime), ThemeResponse.from(theme));
	}

	public static ReservationResponse from(Reservation reservation) {
		return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
				ReservationTimeResponse.from(reservation.getTime()), ThemeResponse.from(reservation.getTheme()));
	}

	public static List<ReservationResponse> from(List<Reservation> reservations) {
		return reservations.stream()
				.map(ReservationResponse::from)
				.collect(Collectors.toList());
	}

}

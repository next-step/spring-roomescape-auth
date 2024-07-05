package roomescape.application.domain.reservationtime.api.dto.response;

import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimes;

import java.util.List;
import java.util.stream.Collectors;

public class FindAllReservationTimesResponse {

    private static final String TIME_PATTERN = "HH:mm";

    private final Long id;

    private final String startAt;

    public FindAllReservationTimesResponse(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    private static FindAllReservationTimesResponse from(ReservationTime time) {
        return new FindAllReservationTimesResponse(time.getId().id(), time.getFormatStartAt(TIME_PATTERN));
    }

    public static List<FindAllReservationTimesResponse> from(ReservationTimes times) {
        return times.getReservationTimes().stream()
                .map(FindAllReservationTimesResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
